package com.omni.besthardware.mappers;

import com.omni.besthardware.rest.dto.response.UsuarioResponse;
import com.omni.besthardware.model.UsuarioModel;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioResponse toUsuarioResponse(UsuarioModel usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}