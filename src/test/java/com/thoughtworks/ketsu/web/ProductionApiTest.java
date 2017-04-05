package com.thoughtworks.ketsu.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.ketsu.support.ApiSupport;
import com.thoughtworks.ketsu.support.ApiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


@RunWith(ApiTestRunner.class)
public class ProductionApiTest extends ApiSupport {

    String basePath = "/productions";

    @Test
    public void should_return_all_productions() throws Exception {

        Response response = target(basePath).request().get();
        assertThat(response.getStatus(), is(200));

        Map result = response.readEntity(Map.class);
        Gson gson = new GsonBuilder().create();

        String jsonStr = gson.toJson(result);
        assertThat(jsonStr,is("{\"productions\":[{\"price\":10.0,\"categoryUri\":\"categories/2\",\"name\":\"apple\",\"id\":1,\"categoryId\":2},{\"price\":5.0,\"categoryUri\":\"categories/3\",\"name\":\"pen\",\"id\":2,\"categoryId\":3},{\"price\":2.0,\"categoryUri\":\"categories/3\",\"name\":\"eraser\",\"id\":3,\"categoryId\":3},{\"price\":6.0,\"categoryUri\":\"categories/2\",\"name\":\"orange\",\"id\":4,\"categoryId\":2},{\"price\":4.0,\"categoryUri\":\"categories/2\",\"name\":\"banana\",\"id\":5,\"categoryId\":2},{\"price\":13.1,\"categoryUri\":\"categories/1\",\"name\":\"success\",\"id\":7,\"categoryId\":1}],\"totalCount\":6}"));
    }

    @Test
    public void should_return_production_by_Id_success() throws Exception {

        Response response = target(basePath + "/2").request().get();
        Map result = response.readEntity(Map.class);

        assertThat(response.getStatus(), is(200));

        assertThat(result.get("id"), is(2));
        assertThat(result.get("name"), is("pen"));
        assertThat(result.get("price"), is(5.0));
        assertThat(result.get("categoryId"), is(3));
    }

    @Test
    public void should_return_production_by_Id_failure() throws Exception {
        Response response = target(basePath + "/0").request().get();
        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_insert_production_success() throws Exception {
        Map data = new HashMap();
        data.put("name", "success");
        data.put("price", 13.1);
        data.put("categoryId", 1);
        Entity entity = Entity.entity(data, MediaType.APPLICATION_JSON_TYPE);
        Response response = target(basePath).request().post(entity);
        assertThat(response.getStatus(), is(201));
    }

    @Test
    public void should_delete_production_success() throws Exception {
        Response response = target(basePath + "/6").request().delete();
        assertThat(response.getStatus(), is(204));
    }

    @Test
    public void should_update_production_success() throws Exception {
        Map data = new HashMap();
        data.put("name", "all");
        data.put("price", 13.1);
        data.put("categoryId", 1);

        Entity entity = Entity.entity(data, MediaType.APPLICATION_JSON_TYPE);
        Response response = target("/productions/1").request().put(entity);
        assertThat(response.getStatus(), is(204));
    }
}