package com.ecommerce.user_service.listener;

import com.ecommerce.user_service.model.Employee;
import com.ecommerce.user_service.service.SequenceGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

@Component
public class EmployeeModelListener extends AbstractMongoEventListener<Employee> {
    private final SequenceGeneratorService sequenceGenerator;

    @Autowired
    public EmployeeModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Employee> event) {
        Employee employee = event.getSource();
        if (employee.getId() == null || employee.getId().isEmpty()) {
            employee.setId(sequenceGenerator.generateEmployeeId());
        }
    }
}