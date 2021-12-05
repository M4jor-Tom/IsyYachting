<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="isyYachtingApp.rental.home.createOrEditLabel"
          data-cy="RentalCreateUpdateHeading"
          v-text="$t('isyYachtingApp.rental.home.createOrEditLabel')"
        >
          Create or edit a Rental
        </h2>
        <div>
          <div class="form-group" v-if="rental.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="rental.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.rental.beginingInstant')" for="rental-beginingInstant"
              >Begining Instant</label
            >
            <div class="d-flex">
              <input
                id="rental-beginingInstant"
                data-cy="beginingInstant"
                type="datetime-local"
                class="form-control"
                name="beginingInstant"
                :class="{ valid: !$v.rental.beginingInstant.$invalid, invalid: $v.rental.beginingInstant.$invalid }"
                required
                :value="convertDateTimeFromServer($v.rental.beginingInstant.$model)"
                @change="updateInstantField('beginingInstant', $event)"
              />
            </div>
            <div v-if="$v.rental.beginingInstant.$anyDirty && $v.rental.beginingInstant.$invalid">
              <small class="form-text text-danger" v-if="!$v.rental.beginingInstant.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.rental.beginingInstant.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.rental.endingInstant')" for="rental-endingInstant"
              >Ending Instant</label
            >
            <div class="d-flex">
              <input
                id="rental-endingInstant"
                data-cy="endingInstant"
                type="datetime-local"
                class="form-control"
                name="endingInstant"
                :class="{ valid: !$v.rental.endingInstant.$invalid, invalid: $v.rental.endingInstant.$invalid }"
                required
                :value="convertDateTimeFromServer($v.rental.endingInstant.$model)"
                @change="updateInstantField('endingInstant', $event)"
              />
            </div>
            <div v-if="$v.rental.endingInstant.$anyDirty && $v.rental.endingInstant.$invalid">
              <small class="form-text text-danger" v-if="!$v.rental.endingInstant.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.rental.endingInstant.ZonedDateTimelocal"
                v-text="$t('entity.validation.ZonedDateTimelocal')"
              >
                This field should be a date and time.
              </small>
            </div>
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
            :disabled="$v.rental.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./rental-update.component.ts"></script>
