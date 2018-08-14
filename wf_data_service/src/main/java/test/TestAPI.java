package test;

import com.qq.connect.utils.Print;
import com.wf.core.utils.http.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shihui
 * @date 2018/6/15
 */
public class TestAPI {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) {
        TestAPI api = new TestAPI();
        api.exec();
    }

    public void exec() {
        String uri = "http://data-api.beeplay123.com/data/api/userLabel/getAllLabels?userId=10001";
        String a = HttpClientUtils.post(uri, logger);
        System.out.println("result:" + a);
    }

}
