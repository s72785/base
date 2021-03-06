/*
 * Copyright 2015 Karlsruhe Institute of Technology.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.kit.dama.mdm.base;

/**
 *
 * @author mf6319
 */
public enum TransitionType {

  NONE("N"),
  DATAWORKFLOW("W"),
  ELASTICSEARCH("E");

  private final String ch;

  TransitionType(String identifier) {
    this.ch = identifier;
  }

  public String getCharacter() {
    return this.ch;
  }

  public static TransitionType translate(String ch) {
    for (TransitionType ts : TransitionType.values()) {
      if (ch.equals(ts.getCharacter())) {
        return ts;
      }
    }

    return null;
  }
}
