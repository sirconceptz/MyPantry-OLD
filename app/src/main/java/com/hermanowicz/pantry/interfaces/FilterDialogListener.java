/*
 * Copyright (c) 2019
 * Mateusz Hermanowicz - All rights reserved.
 * My Pantry
 * https://www.mypantry.eu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hermanowicz.pantry.interfaces;

import com.hermanowicz.pantry.filter.Filter;

/**
 * <h1>FilterDialogListener</h1>
 * Interface to set values from dialog windows.
 *
 * @author Mateusz Hermanowicz
 * @version 1.0
 * @since 1.0
 */
public interface FilterDialogListener {
    void setFilterName(String filterName);

    void setFilterExpirationDate(String filterExpirationDateSince, String filterExpirationDateFor);

    void setFilterProductionDate(String filterProductionDateSince, String filterProductionDateFor);

    void setFilterTypeOfProduct(String filterTypeOfProduct, String filterProductFeatures);

    void setFilterVolume(int filterVolumeSince, int filterVolumeFor);

    void setFilterWeight(int filterWeightSince, int filterWeightFor);

    void setFilterTaste(String filterTaste);

    void setProductFeatures(Filter.Set filterHasSugar, Filter.Set filterHasSalt);
}