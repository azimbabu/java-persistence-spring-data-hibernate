@GenericGenerator(
    name = "ID_GENERATOR",
//    strategy = "enhanced-sequence", // deprecated, use type instead
    type = SequenceStyleGenerator.class,
    parameters = {
        @Parameter(
            name = "sequence_name",
            value = "JPWHSD_SEQUENCE"
        ),
        @Parameter(
            name = "initial_value",
            value = "1000"
        )
    }
)
package com.azimbabu.javapersistence.ch12.fetchloadgraph;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;