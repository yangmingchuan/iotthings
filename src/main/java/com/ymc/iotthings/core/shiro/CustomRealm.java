package com.ymc.iotthings.core.shiro;

import com.ymc.iotthings.model.UserInfo;
import com.ymc.iotthings.service.RolePermService;
import com.ymc.iotthings.service.UserInfoService;
import com.ymc.iotthings.service.UserRoleService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *自定义如何查询用户信息，如何查询用户的角色和权限，如何校验密码等逻辑
 *
 * package name: com.ymc.iotthings.core.shiro
 * date :2019/5/29
 * author : ymc
 **/

public class CustomRealm  extends AuthorizingRealm  {

    @Autowired
    private UserInfoService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RolePermService rolePermService;

    /**
     * 告诉shiro如何根据获取到的用户信息中的密码和盐值来校验密码
     */
    {
        //设置用于匹配密码的CredentialsMatcher
        HashedCredentialsMatcher hashMatcher = new HashedCredentialsMatcher();
        hashMatcher.setHashAlgorithmName("md5");
        hashMatcher.setStoredCredentialsHexEncoded(true);
        //加密的次数
        hashMatcher.setHashIterations(1024);
        this.setCredentialsMatcher(hashMatcher);
    }


    /**
     * 定义如何获取用户的角色和权限的逻辑，给shiro做权限判断
     * @param principalCollection PrincipalCollection
     * @return AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        if (principalCollection == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        UserInfo user = (UserInfo) getAvailablePrincipal(principalCollection);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(user.getRoles());
        info.setStringPermissions(user.getPerms());
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        if (username == null) {
            throw new AccountException("Null usernames are not allowed by this realm.");
        }
        UserInfo userDB = userService.selectBy("userName",username);
        if (userDB == null) {
            throw new UnknownAccountException("No account found for admin [" + username + "]");
        }
        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方
        //SecurityUtils.getSubject().getPrincipal()就能拿出用户的所有信息，包括角色和权限
        List<String> roleList = userRoleService.getRolesByUserId(userDB.getId());
        List<String> permList = rolePermService.getPermsByUserId(userDB.getId());
        Set<String> roles = new HashSet(roleList);
        Set<String> perms = new HashSet(permList);
        userDB.setRoles(roles);
        userDB.setPerms(perms);

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userDB, userDB.getPassword(), getName());
        info.setCredentialsSalt(ByteSource.Util.bytes(userDB.getSalt()));
        return info;
    }
}
