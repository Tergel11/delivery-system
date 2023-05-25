package mn.delivery.system.database.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.*;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Galt
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MongoUtil {

    private final MongoTemplate template;

    public static final FindAndModifyOptions FM_OPTIONS = FindAndModifyOptions.options()
            .collation(Collation.of("mn")).returnNew(true);
    public static final FindAndModifyOptions FM_OPTIONS_WITH_UPSERT = FindAndModifyOptions.options()
            .collation(Collation.of("mn")).upsert(true).returnNew(true);

    public AggregationOptions getAggregationOptions() {
        return new AggregationOptions.Builder().allowDiskUse(true).collation(Collation.of("mn")).build();
    }

    public Aggregation getAggregation(Class<?> sourceClazz, List<AggregationOperation> aggs) {
        return Aggregation.newAggregation(sourceClazz, aggs).withOptions(getAggregationOptions());
    }

    public Aggregation getAggregation(Class<?> sourceClazz, AggregationOperation... aggs) {
        return Aggregation.newAggregation(sourceClazz, aggs).withOptions(getAggregationOptions());
    }

    public <T> TypedAggregation<T> getTypedAggregation(Class<T> sourceClazz, List<AggregationOperation> aggs) {
        return Aggregation.newAggregation(sourceClazz, aggs).withOptions(getAggregationOptions());
    }

    public <T> TypedAggregation<T> getTypedAggregation(Class<T> sourceClazz, AggregationOperation... aggs) {
        return Aggregation.newAggregation(sourceClazz, aggs).withOptions(getAggregationOptions());
    }

    public <T> List<T> getMappedResults(Class<?> sourceClazz, Class<T> targetClazz, String collectionName, List<AggregationOperation> aggs) {
        return template.aggregate(getAggregation(sourceClazz, aggs), collectionName, targetClazz).getMappedResults();
    }

    public <T> List<T> getMappedResults(Class<?> sourceClazz, Class<T> targetClazz, String collectionName, AggregationOperation... aggs) {
        return template.aggregate(getAggregation(sourceClazz, aggs), collectionName, targetClazz).getMappedResults();
    }

    public <T> Document getRawResults(Class<?> sourceClazz, Class<T> targetClazz, String collectionName, List<AggregationOperation> aggs) {
        return template.aggregate(getAggregation(sourceClazz, aggs), collectionName, targetClazz).getRawResults();
    }

    public <T> Document getRawResults(Class<?> sourceClazz, Class<T> targetClazz, String collectionName, AggregationOperation... aggs) {
        return template.aggregate(getAggregation(sourceClazz, aggs), collectionName, targetClazz).getRawResults();
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return template.findAll(clazz);
    }

    public <T> List<T> findAll(Class<T> clazz, String collectionName) {
        return template.findAll(clazz, collectionName);
    }

    public <T> List<T> find(Query query, Class<T> clazz) {
        return template.find(query, clazz);
    }

    public <T> List<T> find(Query query, Class<T> clazz, String collectionName) {
        return template.find(query, clazz, collectionName);
    }

    /*
    *
    *   BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 }}");
        List<Person> result = mongoTemplate.find(query, Person.class);
    * */
    public <T> List<T> find(String query, Class<T> clazz) {
        return template.find(new BasicQuery(query), clazz);
    }

    public <T> T findOne(Query query, Class<T> clazz) {
        return template.findOne(query, clazz);
    }

    public <T> T findOne(Query query, Class<T> clazz, String collectionName) {
        return template.findOne(query, clazz, collectionName);
    }

    public <T> long count(Query query, Class<T> clazz) {
        return template.count(query, clazz);
    }

    public <T> long count(Query query, Class<T> clazz, String collectionName) {
        return template.count(query, clazz, collectionName);
    }

    public <T> T findAndModify(Query query, Update update, Class<T> clazz) {
        return template.findAndModify(query, update, clazz);
    }

    public <T> T findAndModify(Query query, Update update, FindAndModifyOptions options, Class<T> clazz) {
        return template.findAndModify(query, update, options, clazz);
    }

    public <T> T updateSequenceOption(Query query, Class<T> clz, String key) {
        return template.findAndModify(query, new Update().inc(key, 1), FM_OPTIONS, clz);
    }

    public <T> T updateSequenceOptionBy(Query query, Class<T> clz, String key, Integer amountOf) {
        return template.findAndModify(query, new Update().inc(key, amountOf), FM_OPTIONS, clz);
    }

    public <T> T updateSequence(Query query, Class<T> clz, String key) {
        return template.findAndModify(query, new Update().inc(key, 1), clz);
    }

    public <T> T updateSequenceBy(Query query, Class<T> clz, String key, Integer amountOf) {
        return template.findAndModify(query, new Update().inc(key, amountOf), clz);
    }

    /*
    * List<Person> result = template.query(Person.class)
            .matching(query(where("age").lt(50).and("accounts.balance").gt(1000.00d)))
            .all();
    *
    * */
    public <T> List<T> queryMatchingAll(Class<T> clazz, Query query) {
        return template.query(clazz).matching(query).all();
    }

    public <T> List<T> queryMatchingAll(Class<T> clazz, Criteria criteria) {
        return template.query(clazz).matching(criteria).all();
    }

    public <T> Optional<T> queryMatchingOne(Class<T> clazz, Query query) {
        return template.query(clazz).matching(query).one();
    }

    public <T> Optional<T> queryMatchingFirst(Class<T> clazz, Query query) {
        return template.query(clazz).matching(query).first();
    }

    public <T> long queryMatchingCount(Class<T> clazz, Query query) {
        return template.query(clazz).matching(query).count();
    }

    public <T> boolean queryMatchingExists(Class<T> clazz, Query query) {
        return template.query(clazz).matching(query).exists();
    }

    public <T> T save(T object) {
        return template.save(object);
    }

    public <T> T save(T object, String collectionName) {
        return template.save(object, collectionName);
    }

    public static DateOperators.DateToString dateToString(String field, String format) {
        return DateOperators.dateOf(field)
                .withTimezone(DateOperators.Timezone.valueOf("Asia/Ulaanbaatar")).toString(format);
    }

    /*
        string - 2
        objectId - 7;
        double - 1;
        bool - 8;
        date - 9;
        int - 16;
        long - 18;
        decimal - 19;
     */
    public static AggregationOperation convertAndAdd(String convertId, String id, int type) {
        ConvertOperators.Convert convert = ConvertOperators.Convert.convertValue("$" + id);
        return addFields(convertId, convert.to(type));
    }

    public static AggregationOperation convertStringAndAdd(String convertId, String id) {
        return convertAndAdd(convertId, id, 2);
    }

    public static AggregationOperation convertObjectIdAndAdd(String convertId, String id) {
        return convertAndAdd(convertId, id, 7);
    }

    public static ConvertOperators.Convert convertString(String value) {
        return convert(value, 2);
    }

    public static ConvertOperators.Convert convertObjectId(String value) {
        return convert(value, 7);
    }

    public static ConvertOperators.Convert convert(String value, int type) {
        ConvertOperators.Convert convert = ConvertOperators.Convert.convertValue(value);
        return convert.to(type);
    }

    public static AddFieldsOperation addFields(String field, AggregationExpression value) {
        return AddFieldsOperation.addField(field).withValue(value).build();
    }

    public static AddFieldsOperation addFields(String field, String value) {
        return AddFieldsOperation.addField(field).withValue(value).build();
    }

    public static StringOperators.Concat concat(String value) {
        return StringOperators.Concat.valueOf(value);
    }

    public static StringOperators.Concat concat(AggregationExpression value) {
        return StringOperators.Concat.valueOf(value);
    }

    public static StringOperators.Substr substr(String field, int start, int end) {
        StringOperators.Substr substr = StringOperators.Substr.valueOf(field);
        return substr.substring(start, end);
    }

    public static StringOperators.Substr substr(AggregationExpression value, int start, int end) {
        StringOperators.Substr substr = StringOperators.Substr.valueOf(value);
        return substr.substring(start, end);
    }

    public static ArithmeticOperators.Multiply multiply(AggregationExpression value, Number num) {
        ArithmeticOperators.Multiply multiply = ArithmeticOperators.Multiply.valueOf(value);
        return multiply.multiplyBy(num);
    }

    public static ArithmeticOperators.Multiply multiply(String field, Number num) {
        ArithmeticOperators.Multiply multiply = ArithmeticOperators.Multiply.valueOf(field);
        return multiply.multiplyBy(num);
    }

    public static ArithmeticOperators.Divide divide(String field, String divideByField) {
        ArithmeticOperators.Divide divide = ArithmeticOperators.Divide.valueOf(field);
        return divide.divideBy(divideByField);
    }

    public static ConditionalOperators.IfNull ifNull(String ref, String then) {
        return ConditionalOperators.IfNull.ifNull(ref).then(then);
    }

    public static ArrayOperators.ArrayElemAt arrayElemAt(String field, int index) {
        return ArrayOperators.ArrayElemAt.arrayOf(field).elementAt(index);
    }

    public static ConditionalOperators.Cond cond(AggregationExpression exp, String then, String otherWise) {
        return ConditionalOperators.Cond.newBuilder().when(exp).then(then).otherwise(otherWise);
    }

    public static ConditionalOperators.Cond cond(AggregationExpression exp, String then, AggregationExpression otherWise) {
        return ConditionalOperators.Cond.newBuilder().when(exp).then(then).otherwise(otherWise);
    }

    public static ComparisonOperators.Eq eq(String field, Object value) {
        return ComparisonOperators.Eq.valueOf(field).equalToValue(value);
    }

    public void fieldsExclude(Query query, String... fields) {
        if (!ObjectUtils.isEmpty(fields)) {
            query.fields().exclude(fields);
        }
    }

    public void fieldsInclude(Query query, String... fields) {
        if (!ObjectUtils.isEmpty(fields)) {
            query.fields().include(fields);
        }
    }

    public void queryNe(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).ne(value));
        }
    }

    public void queryIn(Query query, Object[] value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).in(value));
        }
    }

    public void queryIn(Query query, List<?> values, String key) {
        if (!ObjectUtils.isEmpty(values)) {
            query.addCriteria(where(key).in(values));
        }
    }

    public void queryIn(Query query, Iterable<?> values, String key) {
        if (!ObjectUtils.isEmpty(values)) {
            query.addCriteria(where(key).in(values));
        }
    }

    public void queryNin(Query query, Object[] value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).nin(value));
        }
    }

    public void queryNin(Query query, List<?> values, String key) {
        if (!ObjectUtils.isEmpty(values)) {
            query.addCriteria(where(key).nin(values));
        }
    }

    public void queryLt(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).lt(value));
        }
    }

    public void queryLte(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).lte(value));
        }
    }

    public void queryGt(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).gt(value));
        }
    }

    public void queryGte(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).gte(value));
        }
    }

    public void queryGtLt(Query query, Object gteValue, Object lteValue, String key) {
        if (!ObjectUtils.isEmpty(gteValue) && !ObjectUtils.isEmpty(lteValue)) {
            query.addCriteria(where(key).gt(gteValue).lt(lteValue));
        }
    }

    public void queryGteLte(Query query, Object gteValue, Object lteValue, String key) {
        if (!ObjectUtils.isEmpty(gteValue) && !ObjectUtils.isEmpty(lteValue)) {
            query.addCriteria(where(key).gte(gteValue).lte(lteValue));
        }
    }

    public void queryRegex(Query query, String value, String key) {
        if (StringUtils.hasLength(value)) {
            query.addCriteria(where(key).regex(value, "i"));
        }
    }

    public void queryRegex(Query query, String value, String key, String options) {
        if (StringUtils.hasLength(value)) {
            query.addCriteria(where(key).regex(value, options));
        }
    }

    public void queryIs(Query query, Object value, String key) {
        if (!ObjectUtils.isEmpty(value)) {
            query.addCriteria(where(key).is(value));
        }
    }

    public Criteria orCriteriaRegex(String value, String key1, String key2) {
        return StringUtils.hasLength(value)
                ? new Criteria().orOperator(where(key1).regex(value, "i"), where(key2).regex(value, "i"))
                : new Criteria();
    }

    public Criteria orCriteriaRegex(String value, String... keys) {
        if (StringUtils.hasLength(value)) {
            Criteria[] criteria = Arrays.stream(keys).map(key -> where(key).regex(value, "i")).toArray(Criteria[]::new);
            return new Criteria().orOperator(criteria);
        }
        return new Criteria();
    }

    public Criteria andCriteriaRegex(String value, String... keys) {
        if (StringUtils.hasLength(value)) {
            Criteria[] criteria = Arrays.stream(keys).map(key -> where(key).regex(value, "i")).toArray(Criteria[]::new);
            return new Criteria().andOperator(criteria);
        }
        return new Criteria();
    }

    public Criteria orCriteriaIs(Object value, String key1, String key2) {
        return !ObjectUtils.isEmpty(value)
                ? new Criteria().orOperator(where(key1).is(value), where(key2).is(value))
                : new Criteria();
    }

    public Criteria orCriteriaIs(Object value, String... keys) {
        if (!ObjectUtils.isEmpty(value)) {
            Criteria[] criteria = Arrays.stream(keys).map(key -> where(key).is(value)).toArray(Criteria[]::new);
            return new Criteria().orOperator(criteria);
        }
        return new Criteria();
    }

    public Criteria andCriteriaIs(Object value, String... keys) {
        if (!ObjectUtils.isEmpty(value)) {
            Criteria[] criteria = Arrays.stream(keys).map(key -> where(key).is(value)).toArray(Criteria[]::new);
            return new Criteria().andOperator(criteria);
        }
        return new Criteria();
    }

    public void queryAndCriteria(Query query, Criteria... criteria) {
        if (criteria.length > 0) {
            query.addCriteria(new Criteria().andOperator(criteria));
        }
    }

    public void queryOrCriteria(Query query, Criteria... criteria) {
        if (criteria.length > 0) {
            query.addCriteria(new Criteria().orOperator(criteria));
        }
    }

    public void queryAndCriteria(Query query, List<Criteria> criteriaList) {
        Criteria[] criteria = new Criteria[criteriaList.size()];
        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(criteria)));
    }

    public void queryPageable(Query query, Pageable pageable) {
        if (pageable != null)
            query.with(pageable);
    }

    public void queryDeleted(Query query, Boolean deleted) {
        query.addCriteria(where("deleted").is(deleted != null && deleted));
    }

    public void queryVisible(Query query, Boolean visible) {
        if (visible != null)
            query.addCriteria(where("visible").is(visible));
    }

    public void queryEnable(Query query, Boolean enable) {
        if (enable != null)
            query.addCriteria(where("enable").is(enable));
    }


    /*
    // { $push : { "category" : { "$each" : [ "spring" , "data" ] } } }
    new Update().push("category").each("spring", "data")

    // { $push : { "key" : { "$position" : 0 , "$each" : [ "Arya" , "Arry" , "Weasel" ] } } }
    new Update().push("key").atPosition(Position.FIRST).each(Arrays.asList("Arya", "Arry", "Weasel"));

    // { $push : { "key" : { "$slice" : 5 , "$each" : [ "Arya" , "Arry" , "Weasel" ] } } }
    new Update().push("key").slice(5).each(Arrays.asList("Arya", "Arry", "Weasel"));

    // { $addToSet : { "values" : { "$each" : [ "spring" , "data" , "mongodb" ] } } }
    new Update().addToSet("values").each("spring", "data", "mongodb");

    template.update(Person.class)
      .matching(query(where("ssn").is(1111).and("firstName").is("Joe").and("Fraizer").is("Update"))
      .apply(update("address", addr))
      .upsert();

      Query query = new Query(Criteria.where("firstName").is("Harry"));
        Update update = new Update().inc("age", 1);

        Person oldValue = template.update(Person.class)
          .matching(query)
          .apply(update)
          .findAndModifyValue(); // return's old person object

    Person newValue = template.query(Person.class)
      .matching(query)
      .findOneValue();

    assertThat(newValue.getAge()).isEqualTo(24);

    Person newestValue = template.update(Person.class)
      .matching(query)
      .apply(update)
      .withOptions(FindAndModifyOptions.options().returnNew(true)) // Now return the newly updated document when updating
      .findAndModifyValue();

    Person upserted = template.update(Person.class)
          .matching(new Query(Criteria.where("firstName").is("Mary")))
          .apply(update)
          .withOptions(FindAndModifyOptions.options().upsert(true).returnNew(true))
          .findAndModifyValue()


    AggregationUpdate update = Aggregation.newUpdate()
        .set("average").toValue(ArithmeticOperators.valueOf("tests").avg())
        .set("grade").toValue(ConditionalOperators.switchCases(
            when(valueOf("average").greaterThanEqualToValue(90)).then("A"),
            when(valueOf("average").greaterThanEqualToValue(80)).then("B"),
            when(valueOf("average").greaterThanEqualToValue(70)).then("C"),
            when(valueOf("average").greaterThanEqualToValue(60)).then("D"))
            .defaultTo("F")
        );

    template.update(Student.class)
        .apply(update)
        .all();


    db.students.update(
       { },
       [
         { $set: { average : { $avg: "$tests" } } },
         { $set: { grade: { $switch: {
                               branches: [
                                   { case: { $gte: [ "$average", 90 ] }, then: "A" },
                                   { case: { $gte: [ "$average", 80 ] }, then: "B" },
                                   { case: { $gte: [ "$average", 70 ] }, then: "C" },
                                   { case: { $gte: [ "$average", 60 ] }, then: "D" }
                               ],
                               default: "F"
         } } } }
       ],
       { multi: true }
    )

        Optional<User> result = template.update(Person.class)
            .matching(query(where("firstame").is("Tom")))
            .replaceWith(new Person("Dick"))
            .withOptions(FindAndReplaceOptions.options().upsert())
            .as(User.class)
            .findAndReplace();

        BasicQuery query = new BasicQuery("{ age : { $lt : 50 }, accounts.balance : { $gt : 1000.00 }}");
        List<Person> result = mongoTemplate.find(query, Person.class);


        List<Person> result = template.query(Person.class)
          .matching(query(where("age").lt(50).and("accounts.balance").gt(1000.00d)))
          .all();

        query.fields().include("lastname");

        query.fields().exclude("id").include("lastname")

        query.fields().include("address")

        query.fields().include("address.city")

        query.fields()
          .project(MongoExpression.create("'$toUpper' : '$last_name'"))
          .as("last_name");

        query.fields()
          .project(StringOperators.valueOf("lastname").toUpper())
          .as("last_name");

        query.fields()
          .project(AggregationSpELExpression.expressionOf("toUpper(lastname)"))
          .as("last_name");

        template.query(Person.class)
          .distinct("lastname")
          .all();

        template.query(Person.class)
          .distinct("lastname")
          .as(String.class)
          .all();


        Query query = TextQuery
          .queryText(new TextCriteria().matchingAny("coffee", "cake"));

        List<Document> page = template.find(query, Document.class);

        TextQuery.queryText(new TextCriteria().matching("coffee").matching("-cake"));
        TextQuery.queryText(new TextCriteria().matching("coffee").notMatching("cake"));

        // search for phrase 'coffee cake'
        TextQuery.queryText(new TextCriteria().matching("\"coffee cake\""));
        TextQuery.queryText(new TextCriteria().phrase("coffee cake"));

        List<Jedi> all = ops.find(SWCharacter.class)
          .as(Jedi.class)
          .matching(query(where("jedi").is(true)))
          .all();

        @Meta(comment = "find luke", batchSize = 100, flags = { SLAVE_OK })
        List<Person> findByFirstname(String firstname);

        Person person = new Person();
        person.setFirstname("Dave");

        Example<Person> example = Example.of(person);

        public interface QueryByExampleExecutor<T> {
          <S extends T> S findOne(Example<S> example);
          <S extends T> Iterable<S> findAll(Example<S> example);
        }

        Person person = new Person();
        person.setFirstname("Dave");

        ExampleMatcher matcher = ExampleMatcher.matching()
          .withIgnorePaths("lastname")
          .withIncludeNullValues()
          .withStringMatcher(StringMatcher.ENDING);

        Example<Person> example = Example.of(person, matcher);

        ExampleMatcher matcher = ExampleMatcher.matching()
          .withMatcher("firstname", endsWith())
          .withMatcher("lastname", startsWith().ignoreCase());

          TypedAggregation<Product> agg = newAggregation(Product.class,
            project("name", "netPrice")
                .and("netPrice").plus(1).as("netPricePlus1")
                .and("netPrice").minus(1).as("netPriceMinus1")
                .and("netPrice").multiply(1.19).as("grossPrice")
                .and("netPrice").divide(2).as("netPriceDiv2")
                .and("spaceUnits").mod(2).as("spaceUnitsMod2"));

          TypedAggregation<Product> agg = newAggregation(Product.class,
            project("name", "netPrice")
                .andExpression("netPrice + 1").as("netPricePlus1")
                .andExpression("netPrice - 1").as("netPriceMinus1")
                .andExpression("netPrice / 2").as("netPriceDiv2")
                .andExpression("netPrice * 1.19").as("grossPrice")
                .andExpression("spaceUnits % 2").as("spaceUnitsMod2")
                .andExpression("(netPrice * 0.8  + 1.2) * 1.19").as("grossPriceIncludingDiscountAndCharge"));

          TypedAggregation<InventoryItem> agg = newAggregation(InventoryItem.class,
              project("item").and("discount")
                .applyCondition(ConditionalOperator.newBuilder().when(Criteria.where("qty").gte(250))
                  .then(30)
                  .otherwise(20))
                .and(ifNull("description", "Unspecified")).as("description")
            );

        template.indexOps(Person.class).ensureIndex(new Index().on("age", Order.DESCENDING).unique());
        List<IndexInfo> indexInfoList = template.indexOps(Person.class).getIndexInfo();

        public class BeforeSaveListener extends AbstractMongoEventListener<Person> {
          @Override
          public void onBeforeSave(BeforeSaveEvent<Person> event) {
            … change values, delete them, whatever …
          }
        }

        @FunctionalInterface
        public interface BeforeSaveCallback<T> extends EntityCallback<T> {


             * Entity callback method invoked before a domain object is saved.
             * Can return either the same or a modified instance.
             * @return the domain object to be persisted.
            T onBeforeSave(T entity <2>, String collection <3>);
        }
        onBeforeConvert
        onBeforeSave
        onAfterSave
        onAfterLoad
        onAfterConvert
    */
}
