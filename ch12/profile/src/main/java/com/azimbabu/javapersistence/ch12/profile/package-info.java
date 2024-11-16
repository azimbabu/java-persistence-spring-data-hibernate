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
@FetchProfiles({
    /**
     * Each profile has a name, this is a simple string we have isolated in a constant.
     */
    @FetchProfile(name = Item.PROFILE_JOIN_SELLER,
        /**
         * Each override in a profile names one entity association or collection.
        */
        fetchOverrides = @FetchProfile.FetchOverride(
            entity = Item.class,
            association = "seller",
            mode = FetchMode.JOIN
        )),
    @FetchProfile(name = Item.PROFILE_JOIN_BIDS,
        fetchOverrides = @FetchProfile.FetchOverride(
            entity = Item.class,
            association = "bids",
            mode = FetchMode.JOIN
        ))
})
package com.azimbabu.javapersistence.ch12.profile;

import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.FetchProfile;
import org.hibernate.annotations.FetchProfiles;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.SequenceStyleGenerator;