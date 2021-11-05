package hr.hsnopek.springjwtrtr.domain.feature.userdevice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.hsnopek.springjwtrtr.domain.feature.user.entity.User;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.entity.UserDevice;
import hr.hsnopek.springjwtrtr.domain.feature.userdevice.repository.UserDeviceRepository;

@Service
public class UserDeviceService {
	
	@Autowired
	private UserDeviceRepository userDeviceRepository;

	public UserDevice createUserDeviceIfNotExists(String deviceId, User user) {
		
		UserDevice userDevice = userDeviceRepository.findOneByDeviceIdAndUserId(deviceId, user.getId());
		
		if(userDevice == null) {
	        userDevice = new UserDevice();
	        userDevice.setDeviceId(deviceId);
	        
	        userDevice.setUser(user);
	        userDeviceRepository.save(userDevice);
		}
		
		return userDevice;
	}
}
