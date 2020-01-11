package org.ops4j.net;

public interface FreePortStrategy {

    /**
     * Finds a free port in the range [from, to]
     * @param from
     * @param to
     * @return
     */
    int findFree(int from, int to);

}
