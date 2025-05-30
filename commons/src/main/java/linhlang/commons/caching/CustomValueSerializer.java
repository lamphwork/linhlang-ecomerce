package linhlang.commons.caching;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

public class CustomValueSerializer<T> implements RedisSerializer<T> {

    private final Class<T> type;

    CustomValueSerializer(Class<T> clazz) {
        this.type = clazz;
    }

    @Override
    public byte[] serialize(Object value) throws SerializationException {

        try (
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(os);
        ) {
            oos.writeObject(value);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        try (
                ByteArrayInputStream is = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(is);
        ) {
            return this.type.cast(ois.readObject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
