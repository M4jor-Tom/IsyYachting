<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="isyYachtingApp.reservation.home.createOrEditLabel"
          data-cy="ReservationCreateUpdateHeading"
          v-text="$t('isyYachtingApp.reservation.home.createOrEditLabel')"
        >
          Create or edit a Reservation
        </h2>
        <div>
          <div class="form-group" v-if="reservation.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="reservation.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.reservation.day')" for="reservation-day">Day</label>
            <select class="form-control" id="reservation-day" data-cy="day" name="day" v-model="reservation.day" required>
              <option v-if="!reservation.day" v-bind:value="null" selected></option>
              <option
                v-bind:value="reservation.day && dayOption.id === reservation.day.id ? reservation.day : dayOption"
                v-for="dayOption in days"
                :key="dayOption.id"
              >
                {{ dayOption.time }}
              </option>
            </select>
          </div>
          <div v-if="$v.reservation.day.$anyDirty && $v.reservation.day.$invalid">
            <small class="form-text text-danger" v-if="!$v.reservation.day.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.reservation.client')" for="reservation-client">Client</label>
            <select class="form-control" id="reservation-client" data-cy="client" name="client" v-model="reservation.client" required>
              <option v-if="!reservation.client" v-bind:value="null" selected></option>
              <option
                v-bind:value="reservation.client && clientOption.id === reservation.client.id ? reservation.client : clientOption"
                v-for="clientOption in clients"
                :key="clientOption.id"
              >
                {{ clientOption.lastName }}
              </option>
            </select>
          </div>
          <div v-if="$v.reservation.client.$anyDirty && $v.reservation.client.$invalid">
            <small class="form-text text-danger" v-if="!$v.reservation.client.required" v-text="$t('entity.validation.required')">
              This field is required.
            </small>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.reservation.rental')" for="reservation-rental">Rental</label>
            <select class="form-control" id="reservation-rental" data-cy="rental" name="rental" v-model="reservation.rental" required>
              <option v-if="!reservation.rental" v-bind:value="null" selected></option>
              <option
                v-bind:value="reservation.rental && rentalOption.id === reservation.rental.id ? reservation.rental : rentalOption"
                v-for="rentalOption in rentals"
                :key="rentalOption.id"
              >
                {{ rentalOption.beginingInstant }}
              </option>
            </select>
          </div>
          <div v-if="$v.reservation.rental.$anyDirty && $v.reservation.rental.$invalid">
            <small class="form-text text-danger" v-if="!$v.reservation.rental.required" v-text="$t('entity.validation.required')">
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
            :disabled="$v.reservation.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./reservation-update.component.ts"></script>
