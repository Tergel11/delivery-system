package mn.delivery.system.database.annotations;

import org.springframework.data.mongodb.repository.Meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Galt
 */

/*

-- Usage

public interface PersonRepository extends CrudReppsitory<Person, String> {
  @AllowDiskUse
  @Aggregation("{ $group: { _id : $lastname, names : { $addToSet : $firstname } } }")
  List<PersonAggregate> groupByLastnameAndFirstnames();
}
* */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Meta(allowDiskUse = true)
public @interface AllowDiskUse {
}
