package com.thoughtworks.ketsu.web;

import com.thoughtworks.ketsu.domain.production.Production;
import com.thoughtworks.ketsu.infrastructure.mybatis.mappers.ProductionMapper;
import org.apache.ibatis.session.SqlSession;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Path("/productions")
public class ProductionApi {

    @Inject
    private SqlSession session;

    @Inject
    private ProductionMapper productionMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Map<String, Object> result = new HashMap<>();

        List<Production> originProductions = productionMapper.findAll();

        List<Map> productions = originProductions
                .stream()
                .map(production -> production.toMap())
                .collect(Collectors.toList());

        result.put("productions", productions);
        result.put("totalCount", productions.size());

        return Response.status(Response.Status.OK).entity(result).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") Integer id) {
        Production production = productionMapper.findById(id);
        if (production == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(production.toMap()).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(Map data) {

        Double price = (Double) data.get("price");
        String name = (String) data.get("name");
        Integer categoryId = 1;
        if(data.get("categoryId") != null) {
            categoryId = (Integer) data.get("categoryId");
        }

        Production production = new Production();
        production.setPrice(price);
        production.setName(name);
        production.setCategoryId(categoryId);

        productionMapper.insert(production);
        Integer id = production.getId();

        session.commit();
        Map result = new HashMap();
        result.put("productionUri", "productions/" + id);

        return Response.status(Response.Status.CREATED).entity(result).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert(@PathParam("id") Integer id) {
        System.out.println(productionMapper.deleteById(id));
        session.commit();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(
            @PathParam("id") Integer id,
            Map data) {
        Production production = new Production();
        production.setId(id);
        production.setName((String) data.get("name"));
        production.setPrice((Double) data.get("price"));
        production.setCategoryId((Integer) data.get("categoryId"));

        productionMapper.update(production);
        session.commit();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
