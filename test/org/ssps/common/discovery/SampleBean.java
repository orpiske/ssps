/**
  Copyright 2012 Otavio Rodolfo Piske

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
package org.ssps.common.discovery;

import java.math.BigDecimal;

public class SampleBean {
    private String first;
    private Integer second;
    private Float third;
    private char fourth; 
    private int fifth;
    private String[] sixth;
    private BigDecimal seventh;
    
    private String with_underscore;
    private String camelCase;
    
    public String getFirst() {
        return first;
    }
    public void setFirst(String first) {
        this.first = first;
    }
    public Integer getSecond() {
        return second;
    }
    public void setSecond(Integer second) {
        this.second = second;
    }
    public Float getThird() {
        return third;
    }
    public void setThird(Float third) {
        this.third = third;
    }
    public char getFourth() {
        return fourth;
    }
    public void setFourth(char fourth) {
        this.fourth = fourth;
    }
    public int getFifth() {
        return fifth;
    }
    public void setFifth(int fifth) {
        this.fifth = fifth;
    }
    public String[] getSixth() {
        return sixth;
    }
    public void setSixth(String[] sixth) {
        this.sixth = sixth;
    }
    public BigDecimal getSeventh() {
        return seventh;
    }
    public void setSeventh(BigDecimal seventh) {
        this.seventh = seventh;
    }
    public String getWithUnderscore() {
	return with_underscore;
    }
    public void setWithUnderscore(String with_underscore) {
	this.with_underscore = with_underscore;
    }
    public String getCamelCase() {
	return camelCase;
    }
    public void setCamelCase(String camelCase) {
	this.camelCase = camelCase;
    }
    

    

}
