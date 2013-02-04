package de.akquinet.jbosscc.needle.db;

import de.akquinet.jbosscc.needle.db.transaction.TransactionHelper;
import de.akquinet.jbosscc.needle.injection.InjectionProvider;
import de.akquinet.jbosscc.needle.injection.InjectionTargetInformation;

class TransactionHelperProvider implements InjectionProvider<TransactionHelper> {

    private final DatabaseTestcase databaseTestcase;

    public TransactionHelperProvider(DatabaseTestcase databaseTestcase) {
        super();
        this.databaseTestcase = databaseTestcase;
    }

    @Override
    public boolean verify(InjectionTargetInformation information) {
        return information.getType() == TransactionHelper.class ? true : false;
    }

    @Override
    public TransactionHelper getInjectedObject(Class<?> injectionPointType) {
        return databaseTestcase.getTransactionHelper();
    }

    @Override
    public Object getKey(InjectionTargetInformation information) {
        return TransactionHelper.class;
    }
}
