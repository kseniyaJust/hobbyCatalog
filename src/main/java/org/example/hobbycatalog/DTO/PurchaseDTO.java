package org.example.hobbycatalog.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseDTO {

    private List<Long> hobbyIds;

    private boolean purchaseAll;
}
