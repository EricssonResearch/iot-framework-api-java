package com.ericsson.research.elasticsearch;

import com.ericsson.research.dataset.Resource;

/**
 * Created by Konstantinos Vandikas on 22/09/14.
 */
public class ResourceSearchResult {

    private String _index;
    private String _type;
    private String _id;
    private float _score;
    private Resource _source;
}
