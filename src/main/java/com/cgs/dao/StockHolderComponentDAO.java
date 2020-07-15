package com.cgs.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface StockHolderComponentDAO {

    String TABLE_NAME = " stock_holder_component ";

    String COLUMNS = " stock_id, organization_type, positions, number_of_shares_held," +
            " proportion_of_tradable_shares, proportion_in_total_equity, release_date ";


}
