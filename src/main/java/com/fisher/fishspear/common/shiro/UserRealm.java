package com.fisher.fishspear.common.shiro;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fisher.fishspear.common.shiro.token.JwtToken;
import com.fisher.fishspear.common.utils.JwtUtil;
import com.fisher.fishspear.entity.SysAdmin;
import com.fisher.fishspear.mapper.SysAdminMapper;
import com.fisher.fishspear.mapper.SysMenuMapper;
import com.fisher.fishspear.mapper.SysRoleMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;


@Component
public class UserRealm extends AuthorizingRealm {

    @Resource
    private SysAdminMapper adminMapper;
    @Resource
    private SysRoleMapper roleMapper;
    @Resource
    private SysMenuMapper menuMapper;

    /**
     * 将自定义token添加到shiro认证中
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    //定义如何获取用户的角色和权限的逻辑，给shiro做权限判断
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        ShiroUser user = (ShiroUser) getAvailablePrincipal(principals);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(user.getRoles());
        info.setStringPermissions(user.getMenus());
        return info;
    }

    //定义如何获取用户信息的业务逻辑，给shiro做登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        String credentials = (String) token.getCredentials();
        String username = JwtUtil.getUsername(credentials);

        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }

        SysAdmin sysAdmin = adminMapper.selectOne(new SysAdmin().setUsername(username).setDel(false));

        if (sysAdmin == null) {
            throw new UnknownAccountException("No account found for admin [" + username + "]");
        }

        ShiroUser admin = new ShiroUser(sysAdmin);
        try {
            JwtUtil.verify(credentials, username, admin.getPassword());
        } catch (JWTVerificationException e) {
            throw new AuthenticationException(e);
        }

        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方就能拿出用户的所有信息，包括角色和权限
        SecurityUtils.getSubject().getPrincipal();
        Set<String> roles = roleMapper.roles(admin.getRoleId());
        Set<String> menus = menuMapper.menus(admin.getRoleId());

        admin.getRoles().addAll(roles);
        admin.getMenus().addAll(menus);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(admin, credentials, getName());
        if (admin.getSalt() != null) {
            info.setCredentialsSalt(ByteSource.Util.bytes(admin.getSalt()));
        }

        return info;
    }
}
