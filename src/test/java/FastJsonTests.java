import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.*;
import com.example.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by alexchen on 2017/9/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.example.Application.class})
public class FastJsonTests {

    /**
     * 普通对象的序列化和反序列化方法
     */
    @Test
    public void testNormal() {
        User u = new User(3L, "alex", 18);
        User1 u1 = new User1(3L, "alex", 18);
        //序列化结果不相同
        Assert.assertNotEquals(JSON.toJSONString(u), JSON.toJSONString(u1));
        //反序列化transient没有起效果
        Assert.assertEquals(JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}", User.class).getName()
                , JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}", User1.class).getName());
    }

    @Test
    public void testIgnore() {
        IgnoreField i = new IgnoreField(3L, "alex", 18);
        IgnoreField1 i1 = new IgnoreField1(3L, "alex", 18);
        //序列化
        Assert.assertEquals(JSON.toJSONString(i), JSON.toJSONString(i1));
        //反序列化 IgnoreField的age字段反序列化配置为false
        Assert.assertNotEquals(JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}", IgnoreField.class).getAge(),
                JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}", IgnoreField1.class).getAge());
    }

    @Test
    public void testOrder() {
        User u = new User(3L, "alex", 18);
        User2 u2 = new User2(3L, "alex", 18);
        //得到的String顺序不同
        Assert.assertNotEquals(JSON.toJSONString(u), JSON.toJSONString(u2));
    }

    @Test
    public void testJSONField() {
        User3 u3 = new User3(1L, null, 13, new Date(1506046315000L), 1000);
        //null值自动变为""
        //覆盖JSONField的features
        System.out.println(JSON.toJSONString(u3));
        Assert.assertEquals(JSON.toJSONString(u3),
                JSON.toJSONString(u3, SerializerFeature.WriteNullStringAsEmpty),
                "{\"ID\":1,\"age\":13,\"birthday\":\"2017-09-22\",\"name\":\"\",\"salary\":\"1000元\"}");
        //字段名不匹配的序列化也成功 age-->old
        Assert.assertNotNull(JSON.parseObject("{\"ID\":1,\"old\":13,\"birthday\":" +
                "\"2017-09-22\",\"salary\":\"1000\"}", User3.class));
    }

    @Test
    public void testList() {
        List<User4> user4List = new ArrayList<>();
        User4 mary = new User4(2L, "Mary", 18, Gender.Female);
        User4 susan = new User4(2L, "Susan", 18, Gender.Female);
        user4List.add(mary);
        user4List.add(susan);
        String[] labels = {"handsome", "tall"};
        User4 tom = new User4(1L, "Tom", 20, Gender.Male, user4List, labels);
        //测试枚举类的处理
        Assert.assertNotEquals(JSON.toJSONString(tom),
                JSON.toJSONString(tom, SerializerFeature.WriteEnumUsingToString));
        System.out.println(JSON.toJSONString(tom));
        System.out.println(JSON.toJSONString(tom, SerializerFeature.WriteEnumUsingToString));
        String a = "[{\"age\":18,\"gender\":\"Female\",\"id\":2,\"name\":\"Mary\"},{\"age\":18,\"gender\":\"Female\",\"id\":2,\"name\":\"Susan\"}]";
        //序列化泛型List
        List<User4> list1 = JSON.parseArray(a, User4.class);
        List<User4> list2 = JSON.parseObject(a, new TypeReference<List<User4>>() {
        });
        Assert.assertEquals(list1, list2);
    }

    @Test
    public void testTime() {
        Time t1 = new Time(new Date(), "", 0L);
        Time t2 = new Time(null, "2017-09-25", 0L);
        Time t3 = new Time(null, "", 1506308576672L);
        //Serialize
        System.out.println(JSON.toJSONStringWithDateFormat(t1, "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(JSON.toJSONString(t1));
        System.out.println(JSON.toJSONStringWithDateFormat(t2, "yyyy-MM-dd HH:mm:ss.SSS"));
        System.out.println(JSON.toJSONString(t1, SerializerFeature.UseISO8601DateFormat));
        //deSerialize
        /**
         *  反序列化能够自动识别如下日期格式
         *  ISO-8601日期格式
         *  yyyy-MM-dd
         *  yyyy-MM-dd HH:mm:ss
         *  yyyy-MM-dd HH:mm:ss.SSS
         *  毫秒数字
         *  毫秒数字字符串
         *  .NET JSON日期格式
         *  new Date(1982932381111)
         */
        System.out.println(JSON.parseObject("{\"date1\":\"2017-09-25 11:11:27.190\",\"date2\":\"\",\"date3\":0}", Time.class));
        System.out.println(JSON.parseObject("{\"date1\":\"2017-09-25T11:11:27.634+08:00\",\"date2\":\"\",\"date3\":0}", Time.class));
        System.out.println(JSON.parseObject("{\"date1\":1506308576672,\"date2\":\"\",\"date3\":0}", Time.class));
    }

    /**
     * SerializeFilter是通过编程扩展的方式定制序列化。fastjson支持6种SerializeFilter，用于不同场景的定制序列化。
     */
    @Test
    public void testFilter() {
        User5 u5first = new User5(1, "alex", 180);
        /**
         * PropertyPreFilter 根据PropertyName判断是否序列化
         * 和PropertyFilter不同只根据object和name进行判断，在调用getter之前，这样避免了getter调用可能存在的异常。
         * 只序列化部分字段
         */
        SimplePropertyPreFilter simplePropertyPreFilter=new SimplePropertyPreFilter(User5.class,new String[]{"id","name"});
        System.out.println("SimplePropertyPreFilter : " + JSON.toJSONString(u5first, simplePropertyPreFilter));
        /**
         * PropertyFilter 根据PropertyName和PropertyValue来判断是否序列化
         * 过滤age大于100的人
         */
        PropertyFilter propertyFilter = (Object source, String name, Object value) -> {
            if (name.equalsIgnoreCase("age") && (int) value > 100) {
                return false;
            } else {
                return true;
            }
        };
        System.out.println("PropertyFilter : " + JSON.toJSONString(u5first, propertyFilter));
        /**
         * NameFilter 修改Key，如果需要修改Key,process返回值则可
         */
        NameFilter nameFilter=(Object source,String name,Object value)->{
            if(name.equals("id")){
                return "ID";
            }
            return name;
        };
        System.out.println("NameFilter :"+JSON.toJSONString(u5first,nameFilter));
        /**
         * ValueFilter 修改Value
         */
        ValueFilter valueFilter=(Object source,String name,Object value)->{
            if(name.equals("name")){
                return "Darwin";
            }
            return value;
        };
        System.out.println("ValueFilter :"+JSON.toJSONString(u5first,valueFilter));
        /**
         * BeforeFilter 序列化时在最前添加内容
         */
        BeforeFilter beforeFilter = new BeforeFilter() {
            @Override
            public void writeBefore(Object object) {
                writeKeyValue("author", "陈煜群");
            }
        };
        System.out.println("BeforeFilter :"+JSON.toJSONString(u5first,beforeFilter));
        /**
         * AfterFilter 序列化时在最后添加内容
         */
        AfterFilter afterFilter = new AfterFilter() {

            @Override
            public void writeAfter(Object object) {
                writeKeyValue("serializeTime", new Date());
            }
        };
        System.out.println("AfterFilter :"+JSON.toJSONString(u5first,afterFilter));
        /**
         * 在某些场景下，对Value做过滤，需要获得所属JavaBean的信息，包括类型、字段、方法等。在fastjson-1.2.9中，
         * 提供了ContextValueFilter，类似于之前版本提供的ValueFilter，只是多了BeanContext参数可用。
         */
        ContextValueFilter contextValueFilter=(BeanContext context, Object object, String name, Object value)->{
            Necessary necessary=context.getField().getAnnotation(Necessary.class);
            if(necessary==null){
                return "necessary_"+value;
            }else{
                return  "unnecessary_"+value;
            }

        };
        System.out.println("ContextValueFilter :"+JSON.toJSONString(u5first,contextValueFilter));
        /**
         * 根据JSONFiled的Label属性过滤
         */
        LabelFilter labelFilter=(String label)->{
            if(label.equals("ignore")){
                return false;
            }else{
                return true;
            }
        };

        System.out.println("LableFilter :"+JSON.toJSONString(u5first,labelFilter));

    }

}
