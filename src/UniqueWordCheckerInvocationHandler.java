import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class UniqueWordCheckerInvocationHandler implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object uniqueWholeWordChecker;
        UniqueWordCheckerLoader loader;
//        if (worker == null) {
//            if (myLoader == null) {
        loader = new UniqueWordCheckerLoader();
//            }
        uniqueWholeWordChecker = loader
                .loadClass("UniqueWholeWordChecker")
                .newInstance();
//        }

        return method.invoke(uniqueWholeWordChecker, args);
    }
}
