package com.haily.zhixing_travel_assistant_backend.config;

import dev.langchain4j.mcp.McpToolProvider;
import dev.langchain4j.mcp.client.DefaultMcpClient;
import dev.langchain4j.mcp.client.McpClient;
import dev.langchain4j.mcp.client.transport.http.StreamableHttpMcpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.util.Streamable;

@Configuration
public class McpToolConfig {


    @Value("${mcp.tools.amap-api-url}")
    private String amapApiUrl;

    @Value("${mcp.tools.variflight-api-url}")
    private String variflightApiUrl;

    @Value("${mcp.tools.time-url}")
    private String timeUrl;

    @Value("${mcp.tools.biying-api-url}")
    private String searchUrl;

    @Value("${mcp.tools.openweather-api-key}")
    private String weatherUrl;

    @Value("${mcp.tools.rollinggohotle-api-key}")
    private String hotelUrl;

    @Value("${mcp.tools.train-url}")
    private String trainUrl;

    /**
     * 高德地图
     * @return
     */
    @Bean
    @Lazy
    public McpClient amapMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(amapApiUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("amapMcpClient")
                .transport(transport)
                .build();
    }

    /**
     * 时间mcp
     * @return
     */
    @Bean
    @Lazy
    public McpClient timeMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(timeUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("timeMcpClient")
                .transport(transport)
                .build();
    }

    /**
     * 航班
     * @return
     */
    @Bean
    @Lazy
    public McpClient flightMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(variflightApiUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("flightMcpClient")
                .transport(transport)
                .build();
    }

    /**
     *aIGoHotel 酒店搜索
     * @return
     */
    @Bean
    @Lazy
    public McpClient hotelMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(hotelUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("hotelMcpClient")
                .transport(transport)
                .build();
    }

    /**
     *必应搜索
     * @return
     */
    @Bean
    @Lazy
    public McpClient biyingSearchMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(searchUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("biyingSearchMcpClient")
                .transport(transport)
                .build();
    }

    /**
     * 火车高铁搜索
     * @return
     */
    @Bean
    @Lazy
    public McpClient trainMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(trainUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("trainMcpClient")
                .transport(transport)
                .build();
    }


    @Bean
    @Lazy
    public McpClient weatherMcpClient(){
        StreamableHttpMcpTransport transport = StreamableHttpMcpTransport.builder()
                .url(weatherUrl)
                .build();

        return new DefaultMcpClient.Builder()
                .key("weatherMcpClient")
                .transport(transport)
                .build();
    }

    //下面给agent锁定对应的mcp

    @Bean("toolsTrain")
    public McpToolProvider mcpToolProvider(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient trainMcpClient,
            McpClient weatherMcpClient,
            McpClient hotelMcpClient,
            McpClient biyingSearchMcpClient,
            McpClient flightMcpClient) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, trainMcpClient, weatherMcpClient, hotelMcpClient, biyingSearchMcpClient, flightMcpClient)
                .build();
    }

    @Bean("toolNormal")
    public McpToolProvider toolsNormal(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient weatherMcpClient,
            McpClient flightMcpClient,
            McpClient hotelMcpClient,
            McpClient trainMcpClient,
            McpClient biyingSearchMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, biyingSearchMcpClient, amapMcpClient, weatherMcpClient, flightMcpClient, hotelMcpClient, trainMcpClient)
                .build();
    }

    @Bean("toolsRequirement")
    public McpToolProvider toolsRequirement(
            McpClient timeMcpClient,
            McpClient weatherMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, weatherMcpClient)
                .build();
    }

    @Bean("toolsAccommodation")
    public McpToolProvider toolsAccommodation(
            McpClient timeMcpClient,
            McpClient hotelMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, hotelMcpClient)
                .build();
    }

    @Bean("toolsFood")
    public McpToolProvider toolsFood(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient weatherMcpClient,
            McpClient biyingSearchMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, weatherMcpClient, biyingSearchMcpClient)
                .build();
    }

    @Bean("toolsDriving")
    public McpToolProvider toolsDriving(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient weatherMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, weatherMcpClient)
                .build();
    }

    @Bean("toolsFlight")
    public McpToolProvider toolsFlight(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient flightMcpClient,
            McpClient weatherMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, flightMcpClient, weatherMcpClient)
                .build();
    }

    @Bean("toolsTrain")
    public McpToolProvider toolsTrain(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient trainMcpClient,
            McpClient weatherMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, trainMcpClient, weatherMcpClient)
                .build();
    }

    @Bean("toolsTransportPlanner")
    public McpToolProvider toolsTransportPlanner(
            McpClient timeMcpClient,
            McpClient amapMcpClient,
            McpClient weatherMcpClient
    ) {
        return McpToolProvider.builder()
                .mcpClients(timeMcpClient, amapMcpClient, weatherMcpClient)
                .build();
    }

}
