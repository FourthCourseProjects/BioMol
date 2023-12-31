package es.ulpgc.model.bio.pools;

import es.ulpgc.model.bio.EnzymePool;
import es.ulpgc.model.bio.enzymes.Ribosome;
import es.ulpgc.model.bio.helixes.ARNm;

public class RibosomePool extends EnzymePool<Ribosome> {

    public RibosomePool() {
        super(Ribosome.class);
    }

    @Override
    public void notify(Object object) {
        action.act(randomEnzyme().translate((ARNm) object));
    }
}
