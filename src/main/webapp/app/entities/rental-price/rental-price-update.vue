<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="isyYachtingApp.rentalPrice.home.createOrEditLabel"
          data-cy="RentalPriceCreateUpdateHeading"
          v-text="$t('isyYachtingApp.rentalPrice.home.createOrEditLabel')"
        >
          Create or edit a RentalPrice
        </h2>
        <div>
          <div class="form-group" v-if="rentalPrice.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="rentalPrice.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.rentalPrice.value')" for="rental-price-value">Value</label>
            <input
              type="number"
              class="form-control"
              name="value"
              id="rental-price-value"
              data-cy="value"
              :class="{ valid: !$v.rentalPrice.value.$invalid, invalid: $v.rentalPrice.value.$invalid }"
              v-model.number="$v.rentalPrice.value.$model"
              required
            />
            <div v-if="$v.rentalPrice.value.$anyDirty && $v.rentalPrice.value.$invalid">
              <small class="form-text text-danger" v-if="!$v.rentalPrice.value.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.rentalPrice.value.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.rentalPrice.season')" for="rental-price-season">Season</label>
            <select
              class="form-control"
              name="season"
              :class="{ valid: !$v.rentalPrice.season.$invalid, invalid: $v.rentalPrice.season.$invalid }"
              v-model="$v.rentalPrice.season.$model"
              id="rental-price-season"
              data-cy="season"
              required
            >
              <option
                v-for="season in seasonValues"
                :key="season"
                v-bind:value="season"
                v-bind:label="$t('isyYachtingApp.Season.' + season)"
              >
                {{ season }}
              </option>
            </select>
            <div v-if="$v.rentalPrice.season.$anyDirty && $v.rentalPrice.season.$invalid">
              <small class="form-text text-danger" v-if="!$v.rentalPrice.season.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.rentalPrice.rental')" for="rental-price-rental">Rental</label>
            <select class="form-control" id="rental-price-rental" data-cy="rental" name="rental" v-model="rentalPrice.rental" required>
              <option v-if="!rentalPrice.rental" v-bind:value="null" selected></option>
              <option
                v-bind:value="rentalPrice.rental && rentalOption.id === rentalPrice.rental.id ? rentalPrice.rental : rentalOption"
                v-for="rentalOption in rentals"
                :key="rentalOption.id"
              >
                {{ rentalOption.beginingInstant }}
              </option>
            </select>
          </div>
          <div v-if="$v.rentalPrice.rental.$anyDirty && $v.rentalPrice.rental.$invalid">
            <small class="form-text text-danger" v-if="!$v.rentalPrice.rental.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.rentalPrice.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./rental-price-update.component.ts"></script>
