package com.round.aside.server.module.VIPCertificate;

import com.round.aside.server.module.IModuleFactory;

public class VIPCertificateModuleFactoryImpl implements IModuleFactory<IVIPCertificate> {

    @Override
    public IVIPCertificate createModule(Object o1, Object... objects) {
        return new VIPCertificateImpl();
    }

}
