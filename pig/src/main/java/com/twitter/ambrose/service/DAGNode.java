/*
Copyright 2012 Twitter, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.twitter.ambrose.service;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Collection;
import java.util.HashSet;

/**
 * Class that represents a Job node in the DAG. The job name must not be null. At DAG creation time
 * the jobID will probably be null. Ideally this will be set on the node when the job is started,
 * and the node will be sent as a <pre>WorkflowEvent.EVENT_TYPE.JOB_STARTED</pre> event.
 *
 * This class can be converted to JSON as-is by doing something like this:
 *
 * ObjectMapper om = new ObjectMapper();
 * om.getSerializationConfig().set(SerializationConfig.Feature.INDENT_OUTPUT, true);
 * String json = om.writeValueAsString(dagNode);
 */
@JsonSerialize(
  include=JsonSerialize.Inclusion.NON_NULL
)
public class DAGNode {
  private String name;
  private String[] aliases;
  private String[] features;
  private String jobId;
  private Collection<DAGNode> successors;
  private Collection<String> successorNames;

  public DAGNode(String name, String[] aliases, String[] features) {
    this.name = name;
    this.aliases = aliases;
    this.features = features;
  }

  public String getName() { return name; }
  public String[] getAliases() { return aliases == null ? new String[0] : aliases; }
  public String[] getFeatures() { return features == null ? new String[0] : features; }

  public String getJobId() { return jobId; }
  public void setJobId(String jobId) { this.jobId = jobId; }

  @JsonIgnore
  public synchronized Collection<DAGNode> getSuccessors() { return successors;}
  public synchronized void setSuccessors(Collection<DAGNode> successors) {
    Collection<String> successorNames = new HashSet<String>();
    if (successors != null) {
      for(DAGNode node : successors) {
        successorNames.add(node.getName());
      }
    }
    this.successors = successors;
    this.successorNames = successorNames;
  }

  public synchronized Collection<String> getSuccessorNames() { return successorNames; }
}
