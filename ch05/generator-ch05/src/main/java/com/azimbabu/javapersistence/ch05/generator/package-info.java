@org.hibernate.annotations.GenericGenerator(
    name = "ID_GENERATOR",
//    strategy = "enhanced-sequence", // deprecated, use type instead
    type = org.hibernate.id.enhanced.SequenceStyleGenerator.class,
    parameters = {
        @org.hibernate.annotations.Parameter(
            name = "sequence_name",
            value = "JPWHSD_SEQUENCE"
        ),
        @org.hibernate.annotations.Parameter(
            name = "initial_value",
            value = "1000"
        ),
        @org.hibernate.annotations.Parameter(
            name = "increment_size",
            value = "1"
        )
    }
)
package com.azimbabu.javapersistence.ch05.generator;