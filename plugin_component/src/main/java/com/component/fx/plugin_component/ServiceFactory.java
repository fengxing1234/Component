package com.component.fx.plugin_component;


import com.component.fx.plugin_component.empty_service.EmptyAccountService;
import com.component.fx.plugin_component.service.IAccountService;

/**
 * 作用:组件间数据传递和方法调用
 * <p>
 * login模块和share模块
 * <p>
 * 其它方式:EventBus，广播，数据持久化等方式
 * <p>
 * 这样的开发模式实现了各个组件间的数据传递都是基于接口编程，接口和实现完全分离，所以就实现了组件间的解耦。
 * 在组件内部的实现类对方法的实现进行修改时，更极端的情况下，我们直接删除、替换了组件时，
 * 只要新加的组件实现了对应 Service 中的抽象方法并在初始化时将实现类对象注册到 ServiceFactory 中，
 * 其他与这个组件有数据传递的组件都不需要有任何修改。
 */
public class ServiceFactory {

    private IAccountService accountService;

    /**
     * 禁止外部创建 ServiceFactory 对象
     */
    private ServiceFactory() {
    }

    private static class Inner {
        private static ServiceFactory serviceFactory = new ServiceFactory();
    }

    /**
     * 通过静态内部类方式实现 ServiceFactory 的单例
     */
    public static ServiceFactory getInstance() {
        return Inner.serviceFactory;
    }


    /**
     * 接收 Login 组件实现的 Service 实例
     */
    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 返回 Login 组件的 Service 实例
     */
    public IAccountService getAccountService() {
        if (accountService == null) {
            accountService = new EmptyAccountService();
        }
        return accountService;
    }

}
