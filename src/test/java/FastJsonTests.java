import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.example.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Type;
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
    public void testNormal(){
        User u=new User(3L,"alex",18);
        User1 u1=new User1(3L,"alex",18);
        //序列化结果不相同
        Assert.assertNotEquals(JSON.toJSONString(u),JSON.toJSONString(u1));
        //反序列化transient没有起效果
        Assert.assertEquals(JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}",User.class).getName()
                ,JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}",User1.class).getName());
    }

    @Test
    public void testIgnore(){
        IgnoreField i=new IgnoreField(3L,"alex",18);
        IgnoreField1 i1=new IgnoreField1(3L,"alex",18);
        //序列化
        Assert.assertEquals(JSON.toJSONString(i),JSON.toJSONString(i1));
        //反序列化 IgnoreField的age字段反序列化配置为false
        Assert.assertNotEquals(JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}",IgnoreField.class).getAge(),
                JSON.parseObject("{\"age\":18,\"id\":3,\"name\":\"alex\"}",IgnoreField1.class).getAge());
    }

    @Test
    public void testOrder(){
        User u=new User(3L,"alex",18);
        User2 u2=new User2(3L,"alex",18);
        //得到的String顺序不同
        Assert.assertNotEquals(JSON.toJSONString(u),JSON.toJSONString(u2));
    }

    @Test
    public void testJSONField(){
        User3 u3=new User3(1L,null,13,new Date(1506046315000L),1000);
        //null值自动变为""
        //覆盖JSONField的features
        System.out.println(JSON.toJSONString(u3));
        Assert.assertEquals(JSON.toJSONString(u3),
                JSON.toJSONString(u3,SerializerFeature.WriteNullStringAsEmpty),
                "{\"ID\":1,\"age\":13,\"birthday\":\"2017-09-22\",\"name\":\"\",\"salary\":\"1000元\"}");
        //字段名不匹配的序列化也成功 age-->old
        Assert.assertNotNull(JSON.parseObject("{\"ID\":1,\"old\":13,\"birthday\":" +
                "\"2017-09-22\",\"salary\":\"1000\"}",User3.class));
    }

    @Test
    public void test(){
        List<User4> user4List=new ArrayList<>();
        User4 mary=new User4(2L,"Mary",18,Gender.Female);
        User4 susan=new User4(2L,"Susan",18,Gender.Female);
        user4List.add(mary);
        user4List.add(susan);
        String[] labels={"handsome","tall"};
        User4 tom=new User4(1L,"Tom",20,Gender.Male,user4List,labels);
        //测试枚举类的处理
        Assert.assertNotEquals(JSON.toJSONString(tom),
                JSON.toJSONString(tom,SerializerFeature.WriteEnumUsingToString));
        System.out.println(JSON.toJSONString(tom));
        System.out.println(JSON.toJSONString(tom,SerializerFeature.WriteEnumUsingToString));
        System.out.println(JSON.toJSONString(user4List));
        String a="[{\"age\":18,\"gender\":\"Female\",\"id\":2,\"name\":\"Mary\"},{\"age\":18,\"gender\":\"Female\",\"id\":2,\"name\":\"Susan\"}]";
        List<User4> list1=JSON.parseArray(a,User4.class);
        List<User4> list2=JSON.parseObject(a, new TypeReference<List<User4>>(){});
        Type type=new TypeReference<List<User4>>(){}.getType();
        List<User4> list3=JSON.parseObject(a, type);


    }


}
