package it.progettois.brewday.service;


import it.progettois.brewday.common.converter.BrewerToFatDtoConverter;
import it.progettois.brewday.common.converter.DtoToBrewerConverter;
import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.persistence.model.Brewer;
import it.progettois.brewday.persistence.repository.BrewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
public class BrewerService implements UserDetailsService {

    private final BrewerRepository brewerRepository;
    private final BrewerToFatDtoConverter brewerToFatDtoConverter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final DtoToBrewerConverter dtoToBrewerConverter;

    @Autowired
    public BrewerService(BrewerRepository brewerRepository,
                         BrewerToFatDtoConverter brewerToFatDtoConverter,
                         BCryptPasswordEncoder bCryptPasswordEncoder,
                         DtoToBrewerConverter dtoToBrewerConverter) {
        this.brewerRepository = brewerRepository;
        this.brewerToFatDtoConverter = brewerToFatDtoConverter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.dtoToBrewerConverter = dtoToBrewerConverter;
    }

    public List<BrewerFatDto> getBrewers() {
        return this.brewerRepository.findAll()
                .stream()
                .map(brewerToFatDtoConverter::convert)
                .collect(Collectors.toList());
    }

    public BrewerFatDto getBrewerByUsername(String username) throws BrewerNotFoundException {
        return this.brewerToFatDtoConverter.convert(this.brewerRepository.findByUsername(username)
                .orElseThrow(BrewerNotFoundException::new));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Brewer brewer = this.brewerRepository.findByUsername(s)
                .orElseThrow(() -> new UsernameNotFoundException(s));

        return new User(brewer.getUsername(), brewer.getPassword(), emptyList());
    }

    public BrewerFatDto saveBrewer(BrewerDto brewerDto) throws ConversionException {

        brewerDto.setPassword(bCryptPasswordEncoder.encode(brewerDto.getPassword()));

        Brewer brewer = this.dtoToBrewerConverter.convert(brewerDto);

        if (brewer == null) {
            throw new ConversionException();
        }

        return this.brewerToFatDtoConverter.convert(this.brewerRepository.save(brewer));
    }
}
