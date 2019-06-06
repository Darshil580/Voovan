package org.voovan.tools.serialize;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.voovan.tools.TByte;
import org.voovan.tools.collection.ObjectThreadPool;
import org.voovan.tools.reflect.TReflect;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProtoStuff 的序列化实现
 *
 * @author: helyho
 * ignite-test Framework.
 * WebSite: https://github.com/helyho/ignite-test
 * Licence: Apache v2 License
 */
public class ProtoStuffSerialize implements Serialize {

    ObjectThreadPool<LinkedBuffer> objectThreadPool = new ObjectThreadPool<LinkedBuffer>(128);

    Map<Class, Schema> SCHEMAS = new ConcurrentHashMap<Class, Schema>();

    public Schema getSchema(Class clazz) {
        Schema schema = SCHEMAS.get(clazz);
        if(schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
        }

        return schema;
    }

    @Override
    public byte[] serialize(Object obj) {
        Schema schema = getSchema(obj.getClass());
        LinkedBuffer buffer =objectThreadPool .get(()->LinkedBuffer.allocate(512));
        byte[] buf = null;
        try {
            buf = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
            byte[] type = (TSerialize.getSimpleNameByClass(obj.getClass())+"\0").getBytes();
            buf = TByte.byteArrayConcat(type,type.length, buf, buf.length);
        } finally {
            buffer.clear();
        }

        return buf;
    }

    @Override
    public <T> T unserialize(byte[] bytes, Class<T> clazz) {
        byte[] type = new byte[512];

        int index = 0;
        for(index=0;index<type.length; index++) {
            if(bytes[index]!='\0') {
                type[index] = bytes[index];
            } else {
                break;
            }
        }
        try {
            String className = new String(type, 0, index, Charset.defaultCharset());

            Class innerClazz = TSerialize.getClassBySimpleName(className);
            Schema schema = getSchema(innerClazz);
            Object obj = null;

            obj = TReflect.newInstance(innerClazz);

            ProtostuffIOUtil.mergeFrom(bytes, index+1, bytes.length-index-1, obj, schema);
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
