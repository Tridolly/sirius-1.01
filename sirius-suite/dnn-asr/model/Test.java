import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Test{

    public static void main(String[] args) throws IOException{
        FileOutputStream fs = new FileOutputStream(new File("/home/sunny/sirius-1.0.1/data.txt"));
        PrintStream p = new PrintStream(fs);
        p.println(100);
        p.close();
        // Bean bean = new Bean();
        // System.out.println(bean.toString());

    }
}

class Bean {

    private String name = "abc";

    private String age = "15";

    private boolean success = true;

    private Long money = 5000L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    @Override
    public String toString(){
        // 要打印出来的日志
        StringBuffer result = new StringBuffer();
        try {
            // 获得当前类的Class
            Class<? extends Bean> clazz = this.getClass();

            // 获取当前类的全部属性
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {

                // 遍历属性得到属性名
                String fieldName = field.getName();

                // 如果是用于序列化的直接过滤掉
                if ("serialVersionUID".equals(fieldName)) {
                    continue;
                }

                // 判断属性的类型，主要是区分boolean和其他类型
                Class<?> type = field.getType();

                // boolean的取值是is,其他是get
                String methodName = (type.getTypeName().equals("boolean")?"is":"get")
                        + fieldName.substring(0,1).toUpperCase()
                        + fieldName.substring(1, fieldName.length());

                Method method;
                java.lang.Object resultObj;

                // 通过方法名得到方法对象
                method = clazz.getMethod(methodName);

                // 得到这个方法的返回值
                resultObj = method.invoke(this);

                // 将属性名和它对应的值进行匹配打印
                if (resultObj != null && !"".equals(resultObj)) {
                    result.append("[").
                            append(fieldName).append("]").append(resultObj).append(" ");
                }
            }
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

}
