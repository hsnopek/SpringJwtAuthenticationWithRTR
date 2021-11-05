package hr.hsnopek.springjwtrtr.domain.feature.userdevice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;

public interface UserDeviceRepository extends JpaRepository<UserDevice, Long>{

	public UserDevice findOneByDeviceId(String deviceId);
	public UserDevice findOneByDeviceIdAndUserId(String deviceId, Long userId);	
}
