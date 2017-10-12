import com.alibaba.fastjson.*;
import com.alibaba.fastjson.serializer.*;
import com.example.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by alexchen on 2017/9/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.example.Application.class})
public class FastJsonTests {

    public FastJsonTests() throws FileNotFoundException {
    }

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

    /**
     * fastjson缺省时会使用字母序序列化，如果你是希望按照java fields/getters的自然顺序序列化，可以配置JSONType.alphabetic
     * 也可以用JSONField中的oridinal来确定顺序
     */
    @Test
    public void testOrder() {
        User u = new User(3L, "alex", 18);
        User2 u2 = new User2(3L, "alex", 18);
        //得到的String顺序不同
        Assert.assertNotEquals(JSON.toJSONString(u), JSON.toJSONString(u2));
    }

    /**
     * JSONField相关属性测试
     */
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

    /**
     * 测试list
     */
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

    /**
     * 时间相关序列化
     */
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
        SimplePropertyPreFilter simplePropertyPreFilter = new SimplePropertyPreFilter(User5.class, new String[]{"id", "name"});
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
        NameFilter nameFilter = (Object source, String name, Object value) -> {
            if (name.equals("id")) {
                return "ID";
            }
            return name;
        };
        System.out.println("NameFilter :" + JSON.toJSONString(u5first, nameFilter));
        /**
         * ValueFilter 修改Value
         */
        ValueFilter valueFilter = (Object source, String name, Object value) -> {
            if (name.equals("name")) {
                return "Darwin";
            }
            return value;
        };
        System.out.println("ValueFilter :" + JSON.toJSONString(u5first, valueFilter));
        /**
         * BeforeFilter 序列化时在最前添加内容
         */
        BeforeFilter beforeFilter = new BeforeFilter() {
            @Override
            public void writeBefore(Object object) {
                writeKeyValue("author", "陈煜群");
            }
        };
        System.out.println("BeforeFilter :" + JSON.toJSONString(u5first, beforeFilter));
        /**
         * AfterFilter 序列化时在最后添加内容
         */
        AfterFilter afterFilter = new AfterFilter() {

            @Override
            public void writeAfter(Object object) {
                writeKeyValue("serializeTime", new Date());
            }
        };
        System.out.println("AfterFilter :" + JSON.toJSONString(u5first, afterFilter));
        /**
         * 在某些场景下，对Value做过滤，需要获得所属JavaBean的信息，包括类型、字段、方法等。在fastjson-1.2.9中，
         * 提供了ContextValueFilter，类似于之前版本提供的ValueFilter，只是多了BeanContext参数可用。
         */
        ContextValueFilter contextValueFilter = (BeanContext context, Object object, String name, Object value) -> {
            Necessary necessary = context.getField().getAnnotation(Necessary.class);
            if (necessary == null) {
                return "necessary_" + value;
            } else {
                return "unnecessary_" + value;
            }

        };
        System.out.println("ContextValueFilter :" + JSON.toJSONString(u5first, contextValueFilter));
        /**
         * 在Fastjson 1.2.7版本之后，fastjson提供LabelFilter功能，用于不同的场景定制序列化。
         */
        LabelFilter labelFilter = (String label) -> {
            if (label.equals("ignore")) {
                return false;
            } else {
                return true;
            }
        };
        System.out.println("LabelFilter :" + JSON.toJSONString(u5first, labelFilter));
        System.out.println("LabelFilter :"+JSON.toJSONString(u5first,Labels.excludes("ignore")));
    }

    /**
     * unwrapped测试类 注意对象属性或者map名字不能重复，虽然不会报错但是会覆盖
     */
    @Test
    public void testUnwrapped(){
        Unwrapped unwrapped=new Unwrapped();
        unwrapped.setId(123);
        unwrapped.setName("Alex");
        unwrapped.setAge(18);
        unwrapped.setLocation(new Location(127,37));
        String text=JSON.toJSONString(unwrapped);
        Assert.assertEquals("{\"age\":18,\"id\":123,\"latitude\":37,\"longitude\":127,\"name\":\"Alex\"}",text);
        Unwrapped unwrapped1=JSON.parseObject(text,Unwrapped.class);
        Assert.assertEquals(unwrapped.getLocation().getLatitude(),unwrapped1.getLocation().getLatitude());
        unwrapped.setLocation(null);
        Map<String,Object> properties=new LinkedHashMap<>();
        properties.put("latitude", 37);
        properties.put("longitude", 127);
        unwrapped.setProperties(properties);
        String text1=JSON.toJSONString(unwrapped);
        Assert.assertEquals("{\"age\":18,\"id\":123,\"name\":\"Alex\",\"latitude\":37,\"longitude\":127}",text1);
    }

    /**
     * <a href="https://github.com/alibaba/fastjson/wiki/Stream-api">Stream api</>
     *
     * @throws IOException
     */
    @Test
    public void testWriter() throws IOException{
        /**
         * 超大JSON数组序列化
         * 如果你的JSON格式是一个巨大的JSON数组，有很多元素，则先调用startArray，然后挨个写入对象，然后调用endArray。
         */
        JSONWriter writer = new JSONWriter(new FileWriter("array.json"));
        writer.startArray();
        for (int i = 0; i < 1000 ; ++i) {
            User user=new User(Long.valueOf(i),"Alex",new Random().nextInt(50));
            writer.writeValue(user);
        }
        writer.endArray();
        writer.close();
        /**
         * 超大JSON对象序列化
         * 如果你的JSON格式是一个巨大的JSONObject，有很多Key/Value对，
         * 则先调用startObject，然后挨个写入Key和Value，然后调用endObject。
         */
        JSONWriter writer2 = new JSONWriter(new FileWriter("object.json"));
        writer2.startObject();
        for (int i = 0; i < 1000 ; ++i) {
            writer2.writeKey("key" + i);
            writer2.writeValue(new User(Long.valueOf(i),"alex",new Random().nextInt(50)));
        }
        writer2.endObject();
        writer2.close();
        //反序列化
        JSONReader reader = new JSONReader(new FileReader("array.json"));
        reader.startArray();
        int count=0;int count2=0;
        while(reader.hasNext()) {
            User vo = reader.readObject(User.class);
            if(vo.getAge()>=18){
                count++;
            }
        }
        reader.endArray();
        reader.close();

        JSONReader reader2 = new JSONReader(new FileReader("object.json"));
        reader2.startObject();
        while(reader2.hasNext()) {
            String key = reader2.readString();
            User user = reader2.readObject(User.class);
            if(user.getId()>=500&&user.getAge()>=18){
                count2++;
            }
            // handle vo ...
        }
        reader2.endObject();
        reader2.close();
        System.out.println("count:"+count+"count2:"+count2);
    }

    /**
     * 在fastjson-1.2.9版本后提供了ExtraProcessable接口，用于定制对象的反序列化功能
     * 如果对象没有对应public setter和public field，就会调用processExtra方法。
     * 这个可以用于一些框架实现MapBean对象的json序列化和反序列化。
     */
    @Test
    public void testExtraProcess(){
        User6 user1 = new User6();
        user1.setName("wenshao");

        String text1 = JSON.toJSONString(user1);

        Assert.assertEquals("{\"name\":\"wenshao\"}", text1);
        String text2 = "{\"name\":\"wenshao\",\"id\":1001}";

        User6 user2 = JSON.parseObject(text2, User6.class);
        ExtraProcess extraProcess=JSON.parseObject(text2,ExtraProcess.class);
        Assert.assertEquals("wenshao", user2.getName());
        Assert.assertEquals(1001, user2.getAttribute("id"));
    }

    /**
     * @see <a herf="https://github.com/alibaba/fastjson/wiki/JSONPath"></>
     */
    @Test
    public void JSONPath(){
        User8 u1=new User8(1,"alex",new Object());
        User8 u2=new User8(2,"susan",new Object());
        User8 u3=new User8(3,"sara");
        List<User8> uList=Arrays.asList(u1,u2,u3);
        List<User8> result=(List<User8>)JSONPath.eval(uList,"[1,2]");
        List<User8> uList2=(List<User8>)JSONPath.eval(uList,"[id=1]");
        Assert.assertSame(JSONPath.eval(u1,"$.id"),1);
        Assert.assertEquals(u1,uList2.get(0));
    }

    @Test
    public void testJSONCreator(){
        User9 u9=new User9(1,"Alex");
        System.out.println(JSON.toJSONString(u9));
        System.out.println(JSON.parseObject("{\"ID\":1,\"name\":\"Alex\"}",User9.class));
        User10 u10 = JSON.parseObject("{\"id\":1,\"name\":\"ljw\"}", User10.class);
        Assert.assertEquals(u10.getId().intValue(),1 );
        Assert.assertEquals("ljw", u10.getName());

    }
}
