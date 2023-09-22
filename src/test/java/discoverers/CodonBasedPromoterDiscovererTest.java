package discoverers;

import es.ulpgc.model.bio.helixes.Helix;
import es.ulpgc.model.bio.helixes.generators.CodonBasedPromoterDiscoverer;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static es.ulpgc.model.bio.Chromatin.Promoter;
import static es.ulpgc.model.bio.acids.nucleic.NucleicAcid.*;
import static es.ulpgc.model.bio.helixes.generators.CodonBasedPromoterDiscoverer.startCodon;
import static es.ulpgc.model.bio.helixes.generators.CodonBasedPromoterDiscoverer.stopCodons;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CodonBasedPromoterDiscovererTest {
    CodonBasedPromoterDiscoverer discoverer;

    @Before
    public void setUp() {
        discoverer = new CodonBasedPromoterDiscoverer();
    }

    @Test
    public void should_return_promoter_0_to_8() {
        Helix helix = new Helix(List.of(startCodon.a(), startCodon.b(), startCodon.c(),
                                        Adenine, Guanine, Cytosine,
                                        stopCodons.get(0).a(), stopCodons.get(0).b(), stopCodons.get(0).c()));
        assertThat(discoverer.discover(helix)).isEqualTo(List.of(new Promoter(0,8)));
    }

    @Test
    public void should_return_promoter_0_to_8_without_stop_codon() {
        Helix helix = new Helix(List.of(startCodon.a(), startCodon.b(), startCodon.c(),
                                        Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine));
        assertThat(discoverer.discover(helix)).isEqualTo(List.of(new Promoter(0,8)));
    }

    @Test
    public void should_return_two_promoters() {
        Helix helix = new Helix(List.of(startCodon.a(), startCodon.b(), startCodon.c(),
                                        Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine,
                                        startCodon.a(), startCodon.b(), startCodon.c(),
                                        stopCodons.get(0).a(), stopCodons.get(0).b(), stopCodons.get(0).c(),
                                        Adenine, Guanine, Cytosine));
        assertThat(discoverer.discover(helix)).isEqualTo(List.of(new Promoter(0, 14), new Promoter(8, 14)));
    }

    @Test
    public void should_return_two_promoters_without_stop_codon() {
        Helix helix = new Helix(List.of(startCodon.a(), startCodon.b(), startCodon.c(),
                Adenine, Guanine, Cytosine,
                Adenine, Guanine, Cytosine,
                startCodon.a(), startCodon.b(), startCodon.c(),
                Adenine, Guanine, Cytosine));
        assertThat(discoverer.discover(helix)).isEqualTo(List.of(new Promoter(0,14), new Promoter(8,14)));
    }

    @Test
    public void should_return_no_promoters() {
        Helix helix = new Helix(List.of(Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine,
                                        Adenine, Guanine, Cytosine));
        assertThat(discoverer.discover(helix)).isEqualTo(List.of());
    }
}