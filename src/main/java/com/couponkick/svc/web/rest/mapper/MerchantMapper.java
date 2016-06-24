package com.couponkick.svc.web.rest.mapper;

import com.couponkick.svc.domain.*;
import com.couponkick.svc.web.rest.dto.MerchantDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Merchant and its DTO MerchantDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MerchantMapper {

    @Mapping(source = "billingAddress.id", target = "billingAddressId")
    @Mapping(source = "shippingAddress.id", target = "shippingAddressId")
    MerchantDTO merchantToMerchantDTO(Merchant merchant);

    List<MerchantDTO> merchantsToMerchantDTOs(List<Merchant> merchants);

    @Mapping(source = "billingAddressId", target = "billingAddress")
    @Mapping(source = "shippingAddressId", target = "shippingAddress")
    Merchant merchantDTOToMerchant(MerchantDTO merchantDTO);

    List<Merchant> merchantDTOsToMerchants(List<MerchantDTO> merchantDTOs);

    default Address addressFromId(Long id) {
        if (id == null) {
            return null;
        }
        Address address = new Address();
        address.setId(id);
        return address;
    }
}
