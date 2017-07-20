package org.ar4j.elasticsearch.test;

import junit.framework.TestCase;
import org.apache.commons.collections.CollectionUtils;
import org.ar4j.QueryResponse;
import org.ar4j.elasticsearch.ElasticSearchActiveRecord;
import org.ar4j.elasticsearch.ElasticSearchException;
import org.ar4j.utils.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ElasticSearchActiveRecordTest extends TestCase {
    private ElasticSearchActiveRecord activeRecord = new ElasticSearchActiveRecord();

    @Before
    public void init() {
    }

    @After
    public void exit() {
    }

    @Test
    public void testSearch() throws ElasticSearchException {
        QueryResponse response = new DataEntity().query();
        System.out.println(response.getTotal());
    }

    @Test
    public void testInsert() throws ElasticSearchException {
        DataEntity data = new DataEntity();
        data.setId("testId");
        data.setStrField("test str11110000");
        data.setIntegerField(null);
        data.setLongField(100000L);
        data.setFloatField(10.00300F);
        data.setDoubleField(1000.0230984000);

        data.insert(data);
    }

    @Test
    public void testGet() throws ElasticSearchException {
        String id = "testId";
        DataEntity data =  activeRecord.get(DataEntity.class, id);
        System.out.println(JsonUtils.toJson(data));
    }

    @Test
    public void testUpdate() throws ElasticSearchException {
        DataEntity data = new DataEntity();
        data.setId("testId");
        data.setStrField("test str111");
        data.setIntegerField(100111);
        data.setLongField(100000111L);
        data.setFloatField(null);
        data.setDoubleField(1000.0230984111);

        data.update(data);
    }

    @Test
    public void testDelete() throws ElasticSearchException {
        String id = "testId";
        int rows =  activeRecord.delete(DataEntity.class, id);
    }

    @Test
    public void testUpsert() throws ElasticSearchException {
        DataEntity data = new DataEntity();
        data.setId("testId2");
        data.setStrField("test str111222");
        data.setIntegerField(100111222);
        data.setLongField(null);
        data.setFloatField(22.4234F);
        data.setDoubleField(1000.0230984111222);

        data.upsert(data);
    }

    @Test
    public void testBatchGet() throws ElasticSearchException {
        String[] ids = new String[] {"id1", "id2", "id4"};
        List<String> idList = new ArrayList<String>();
        CollectionUtils.addAll(idList, ids);
        List<DataEntity> list = activeRecord.get(DataEntity.class, idList);
        for (DataEntity data : list)
            System.out.println(JsonUtils.toJson(data));
    }

    @Test
    public void testBatchInsert() throws ElasticSearchException {
        DataEntity data1 = new DataEntity();
        data1.setId("id1");
        data1.setStrField("test str1");
        data1.setIntegerField(111);
        data1.setLongField(111111L);
        data1.setFloatField(11.111F);
        data1.setDoubleField(111.111111);

        DataEntity data2 = new DataEntity();
        data2.setId("id2");
        data2.setStrField("test str2");
        data2.setIntegerField(222);
        data2.setLongField(222222L);
        data2.setFloatField(22.222F);
        data2.setDoubleField(222.222222);

        DataEntity data3 = new DataEntity();
        data3.setId("id3");
        data3.setStrField("test str3");
        data3.setIntegerField(333);
        data3.setLongField(333333L);
        data3.setFloatField(33.333F);
        data3.setDoubleField(333.333333);

        List<DataEntity> list = new LinkedList<>();
        list.add(data1);
        list.add(data2);
        list.add(data3);

        activeRecord.insert(list);
    }

    @Test
    public void testBatchUpdate() throws ElasticSearchException {
        DataEntity data1 = new DataEntity();
        data1.setId("id1");
        data1.setStrField("test str17771122");
        data1.setIntegerField(111);
        data1.setLongField(111111777L);
        data1.setFloatField(null);
        data1.setDoubleField(111.111111);

        DataEntity data2 = new DataEntity();
        data2.setId("id2");
        data2.setStrField("test str288881122");
        data2.setIntegerField(222888);
        data2.setLongField(222222L);
        data2.setFloatField(22.222888F);
        data2.setDoubleField(null);

        DataEntity data3 = new DataEntity();
        data3.setId("id3");
        data3.setStrField("test str39991122");
        data3.setIntegerField(null);
        data3.setLongField(333333L);
        data3.setFloatField(33.333F);
        data3.setDoubleField(333.333333);

        DataEntity data4 = new DataEntity();
        data4.setId("id4");
        data4.setStrField("test str399911");
        data4.setIntegerField(null);
        data4.setLongField(333333L);
        data4.setFloatField(33.333F);
        data4.setDoubleField(333.333333);

        DataEntity data5 = new DataEntity();
        data5.setId("id5");
        data5.setStrField("test str399911");
        data5.setIntegerField(null);
        data5.setLongField(333333L);
        data5.setFloatField(33.333F);
        data5.setDoubleField(333.333333);

        List<DataEntity> list = new LinkedList<>();
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);

        int rows = activeRecord.update(list);
        System.out.println(rows);
    }

    @Test
    public void testBatchUpsert() throws ElasticSearchException {
        DataEntity data1 = new DataEntity();
        data1.setId("id1");
        data1.setStrField("test str1");
        data1.setIntegerField(111);
        data1.setLongField(111111L);
        data1.setFloatField(11.111F);
        data1.setDoubleField(111.111111);

        DataEntity data2 = new DataEntity();
        data2.setId("id2");
        data2.setStrField("test str2");
        data2.setIntegerField(222);
        data2.setLongField(222222L);
        data2.setFloatField(null);
        data2.setDoubleField(222.222222);

        DataEntity data3 = new DataEntity();
        data3.setId("id3");
        data3.setStrField("test str3");
        data3.setIntegerField(333);
        data3.setLongField(333333L);
        data3.setFloatField(33.333F);
        data3.setDoubleField(333.333333);

        DataEntity data4 = new DataEntity();
        data4.setId("id4");
        data4.setStrField("test str399911");
        data4.setIntegerField(null);
        data4.setLongField(333333L);
        data4.setFloatField(33.333F);
        data4.setDoubleField(333.333333);

        DataEntity data5 = new DataEntity();
        data5.setId("id5");
        data5.setStrField("test str399911");
        data5.setIntegerField(null);
        data5.setLongField(333333L);
        data5.setFloatField(33.333F);
        data5.setDoubleField(333.333333);

        List<DataEntity> list = new LinkedList<>();
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);

        int rows = activeRecord.upsert(list);
        System.out.println(rows);
    }

    @Test
    public void testBatchDelete() throws ElasticSearchException {
        String[] ids = new String[] {"id1", "id2", "id3", "id9", "id5"};
        List<String> idList = new ArrayList<>();
        CollectionUtils.addAll(idList, ids);
        int rows = activeRecord.delete(DataEntity.class, idList);
        System.out.println(rows);
    }

    @Test
    public void testCreate() throws ElasticSearchException {
        activeRecord.create(FunData.class);
    }
}
