package hr.hsnopek.springjwtrtr.domain.feature.role.enumeration;

import java.util.Arrays;

public enum RoleNameEnum {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
    
    private final String roleName;

    private RoleNameEnum(String roleName) {
        this.roleName = roleName;
    }
	
    public static RoleNameEnum fromString(String roleName) throws IllegalArgumentException {
        return Arrays.stream(RoleNameEnum.values())
                .filter(x -> x.roleName.equals(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown value: " + roleName));
    }

	@Override
	public String toString() {
		return roleName;
	}
    
}
