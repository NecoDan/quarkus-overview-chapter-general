package br.com.daniel.java.quarkus.general.adapter.out.dto.uol_challenge;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@XmlRootElement(name = "liga_da_justica")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JusticeLeagueDcDTO {

    private List<String> codinomes;

    // O XmlElementWrapper mapeia a tag <codinomes> que engloba a lista
    // O XmlElement define o nome de cada item individual (<codinome>)
    @XmlElementWrapper(name = "codinomes")
    @XmlElement(name = "codinome")
    public List<String> getCodinomes() {
        return codinomes;
    }
}

