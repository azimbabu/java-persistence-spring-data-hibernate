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
        )
    }
)
@org.hibernate.annotations.FilterDefs({
    @org.hibernate.annotations.FilterDef(
        name = "limitByUserRanking",
        parameters = {
            @org.hibernate.annotations.ParamDef(
                name = "currentUserRanking", type = Integer.class
            )
        }
    ),
    @org.hibernate.annotations.FilterDef(
        name = "limitByUserRankingDefault",
        defaultCondition = """
            :currentUserRanking >= (
            select u.RANKING from USERS u
            where u.ID = SELLER_ID
          )
          """,
        parameters = {
            @org.hibernate.annotations.ParamDef(
                name = "currentUserRanking", type = Integer.class
            )
        }
    )
})
package com.azimbabu.javapersistence.ch13;