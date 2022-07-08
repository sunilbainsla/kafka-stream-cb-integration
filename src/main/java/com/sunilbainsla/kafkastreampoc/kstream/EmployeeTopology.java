package com.sunilbainsla.kafkastreampoc.kstream;

import lombok.extern.log4j.Log4j2;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Predicate;
import org.apache.kafka.streams.kstream.Printed;

import java.util.function.Function;

@Log4j2

public class EmployeeTopology implements Function<KStream<String, Employee>, KStream<String, Employee>[]> {

    public KStream<String, Employee>[] apply(KStream<String, Employee> employeeKStream) {
        employeeKStream.peek((k, v) -> System.out.println("Data------>" + v));
        employeeKStream.groupBy(
                        (s, employee) -> employee.getDepartment()).
                aggregate(() ->
                                new DepartmentAggregate()
                                        .withEmployeeCount(0)
                                        .withAvgSalary(0.0)
                                        .withTotalSalary(0),
                        (k, employee, departmentAggregate) ->
                                new DepartmentAggregate()
                                        .withTotalSalary(departmentAggregate.getTotalSalary() + employee.getSalary())
                                        .withAvgSalary(departmentAggregate.getAvgSalary() + employee.getSalary()
                                                / departmentAggregate.getEmployeeCount() + 1D
                                        )
                                        .withEmployeeCount(departmentAggregate.getEmployeeCount() + 1),
                        Materialized.as("MyStoreName"));
        employeeKStream.print(Printed.toSysOut());
        KStream<String, Employee>[] outputStream = new KStream[]{employeeKStream};

        return outputStream;
    }

    Predicate<String, Employee> paymentPredicate = (key, message) -> message.getName().contains("Sunil");

}
