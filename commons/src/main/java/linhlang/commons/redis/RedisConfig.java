package linhlang.commons.redis;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableAutoConfiguration(exclude = {
        RedisAutoConfiguration.class,
        RedisRepositoriesAutoConfiguration.class
})
@Import({RedisProperties.class, RedisLock.class})
@ConditionalOnProperty(value = "redis.enabled", havingValue = "true")
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    @Bean
    public RedisConnectionFactory connectionFactory(RedisProperties properties) {
        String nodes = properties.getNodes();
        String username = properties.getUsername();
        String password = properties.getPassword();

        if (nodes == null || nodes.isBlank()) {
            throw new IllegalArgumentException("redis.nodes must not be empty if redis.enabled is true");
        }

        List<RedisNode> redisNodes = readNodes(nodes);
        if (redisNodes.size() == 1) {
            return standaloneConnectionFactory(redisNodes.getFirst(), username, password);
        }
        if (redisNodes.size() > 1) {
            return clusterConnectionFactory(redisNodes, username, password);
        }

        throw new IllegalArgumentException("redis.nodes value invalid");
    }

    /**
     * read redis node from properties
     *
     * @param nodes properties
     * @return redis nodes
     */
    private List<RedisNode> readNodes(String nodes) {
        return Arrays.stream(nodes.split(",")).map(nodeStr -> {
            String[] node = nodeStr.split(":");
            return new RedisNode(node[0], Integer.parseInt(node[1]));
        }).toList();
    }

    /**
     * create connection factory standalone mode
     *
     * @param redisNode redis node
     * @param username  username
     * @param password  password
     * @return connection factory
     */
    RedisConnectionFactory standaloneConnectionFactory(RedisNode redisNode, String username, String password) {
        var configuration = new RedisStandaloneConfiguration(
                Objects.requireNonNull(redisNode.getHost()),
                Objects.requireNonNull(redisNode.getPort())
        );
        configuration.setUsername(username);
        configuration.setPassword(password);
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * create connection factory cluster mode
     *
     * @param redisNodes redis nodes
     * @param username   username
     * @param password   password
     * @return connection factory
     */
    RedisConnectionFactory clusterConnectionFactory(List<RedisNode> redisNodes, String username, String password) {
        var configuration = new RedisClusterConfiguration();
        configuration.setClusterNodes(redisNodes);
        configuration.setUsername(username);
        configuration.setPassword(getPassword(password));
        return new LettuceConnectionFactory(configuration);
    }

    /**
     * read redis password
     *
     * @param password raw password
     * @return redis password
     */
    RedisPassword getPassword(String password) {
        return Optional.ofNullable(password)
                .filter(str -> !str.isEmpty())
                .map(RedisPassword::of)
                .orElse(RedisPassword.none());
    }

}
