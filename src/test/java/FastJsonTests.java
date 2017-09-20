import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.asm.Type;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.example.entity.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by alexchen on 2017/9/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.example.Application.class})
public class FastJsonTests {

    private String s="{\"age\":18,\"id\":3,\"name\":\"alex\"}";
    /**
     * 普通对象的序列化和反序列化方法
     */
    @Test
    public void test(){
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
        System.out.println(JSON.parseObject(s,IgnoreField.class));
        System.out.println(JSON.parseObject(s,IgnoreField1.class));
        //序列化
        Assert.assertEquals(JSON.toJSONString(i),JSON.toJSONString(i1));
    }

    @Test
    public void testOrder(){
        User u=new User(3L,"alex",18);
        User2 u2=new User2(3L,"alex",18);
        Assert.assertNotEquals(JSON.toJSONString(u),JSON.toJSONString(u2));
    }
}
