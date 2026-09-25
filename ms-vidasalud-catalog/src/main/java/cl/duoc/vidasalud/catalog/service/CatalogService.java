package cl.duoc.vidasalud.catalog.service;

import cl.duoc.vidasalud.catalog.domain.Box;
import cl.duoc.vidasalud.catalog.domain.MedicalService;
import cl.duoc.vidasalud.catalog.dto.*;
import cl.duoc.vidasalud.catalog.exception.NoSlotsAvailableException;
import cl.duoc.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.duoc.vidasalud.catalog.repository.BoxRepository;
import cl.duoc.vidasalud.catalog.repository.MedicalServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CatalogService {
    private final MedicalServiceRepository serviceRepository;
    private final BoxRepository boxRepository;

    public CatalogService(MedicalServiceRepository serviceRepository, BoxRepository boxRepository) {
        this.serviceRepository = serviceRepository;
        this.boxRepository = boxRepository;
    }

    @Transactional(readOnly = true)
    public List<MedicalServiceResponse> listServices() {
        return serviceRepository.findAllByOrderByNameAsc().stream().map(this::toServiceResponse).toList();
    }

    public MedicalServiceResponse createService(MedicalServiceRequest request) {
        MedicalService entity = new MedicalService();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setActive(request.active());
        return toServiceResponse(serviceRepository.save(entity));
    }

    public MedicalServiceResponse updateService(Long id, MedicalServiceRequest request) {
        MedicalService entity = serviceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la prestación " + id + "."));
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setPrice(request.price());
        entity.setActive(request.active());
        return toServiceResponse(serviceRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public List<BoxResponse> listBoxes() {
        return boxRepository.findAllByOrderByNameAsc().stream().map(this::toBoxResponse).toList();
    }

    public BoxResponse createBox(BoxRequest request) {
        if (!serviceRepository.existsById(request.serviceId())) {
            throw new ResourceNotFoundException("No se encontró la prestación " + request.serviceId() + ".");
        }
        Box entity = new Box();
        entity.setName(request.name());
        entity.setServiceId(request.serviceId());
        entity.setAvailableSlots(request.availableSlots());
        entity.setActive(request.active());
        return toBoxResponse(boxRepository.save(entity));
    }

    public BoxResponse reserveBox(Long id) {
        Box entity = boxRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el box " + id + "."));
        if (!entity.isActive() || entity.getAvailableSlots() <= 0) {
            throw new NoSlotsAvailableException(id);
        }
        entity.setAvailableSlots(entity.getAvailableSlots() - 1);
        return toBoxResponse(boxRepository.save(entity));
    }

    private MedicalServiceResponse toServiceResponse(MedicalService entity) {
        return new MedicalServiceResponse(entity.getId(), entity.getName(), entity.getDescription(), entity.getPrice(), entity.isActive());
    }

    private BoxResponse toBoxResponse(Box entity) {
        return new BoxResponse(entity.getId(), entity.getName(), entity.getServiceId(), entity.getAvailableSlots(), entity.isActive());
    }
}
