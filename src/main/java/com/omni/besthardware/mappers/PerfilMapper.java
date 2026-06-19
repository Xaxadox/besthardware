package com.omni.besthardware.mappers;

import com.omni.besthardware.model.PerfilModel;
import com.omni.besthardware.rest.dto.response.PerfilResponse;
import org.springframework.stereotype.Component;

@Component
public class PerfilMapper {

    private final ComponenteMapper componenteMapper;

    public PerfilMapper(ComponenteMapper componenteMapper) {
        this.componenteMapper = componenteMapper;
    }

    public PerfilResponse toPerfilResponse(PerfilModel perfil) {
        return new PerfilResponse(
                perfil.getId(),
                perfil.getNome(),
                perfil.getComponentes().stream()
                        .map(componenteMapper::toComponenteResponse)
                        .toList()
        );
    }
}
