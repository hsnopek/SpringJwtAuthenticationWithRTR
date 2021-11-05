package hr.hsnopek.springjwtrtr.domain.feature.role.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import hr.hsnopek.springjwtrtr.domain.feature.role.enumeration.RoleNameEnum;

@Converter
public class RoleNameConverter implements AttributeConverter<RoleNameEnum, String> {

	@Override
	public String convertToDatabaseColumn(RoleNameEnum roleNameEnum) {
		return roleNameEnum.toString();
	}

	@Override
	public RoleNameEnum convertToEntityAttribute(String roleName) {
		return RoleNameEnum.fromString(roleName);
	}

}
