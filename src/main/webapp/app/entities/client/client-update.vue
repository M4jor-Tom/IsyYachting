<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="isyYachtingApp.client.home.createOrEditLabel"
          data-cy="ClientCreateUpdateHeading"
          v-text="$t('isyYachtingApp.client.home.createOrEditLabel')"
        >
          Create or edit a Client
        </h2>
        <div>
          <div class="form-group" v-if="client.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="client.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.client.firstName')" for="client-firstName">First Name</label>
            <input
              type="text"
              class="form-control"
              name="firstName"
              id="client-firstName"
              data-cy="firstName"
              :class="{ valid: !$v.client.firstName.$invalid, invalid: $v.client.firstName.$invalid }"
              v-model="$v.client.firstName.$model"
              required
            />
            <div v-if="$v.client.firstName.$anyDirty && $v.client.firstName.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.firstName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.client.lastName')" for="client-lastName">Last Name</label>
            <input
              type="text"
              class="form-control"
              name="lastName"
              id="client-lastName"
              data-cy="lastName"
              :class="{ valid: !$v.client.lastName.$invalid, invalid: $v.client.lastName.$invalid }"
              v-model="$v.client.lastName.$model"
              required
            />
            <div v-if="$v.client.lastName.$anyDirty && $v.client.lastName.$invalid">
              <small class="form-text text-danger" v-if="!$v.client.lastName.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.client.phone')" for="client-phone">Phone</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="client-phone"
              data-cy="phone"
              :class="{ valid: !$v.client.phone.$invalid, invalid: $v.client.phone.$invalid }"
              v-model="$v.client.phone.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.client.email')" for="client-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="client-email"
              data-cy="email"
              :class="{ valid: !$v.client.email.$invalid, invalid: $v.client.email.$invalid }"
              v-model="$v.client.email.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('isyYachtingApp.client.nationality')" for="client-nationality">Nationality</label>
            <select
              class="form-control"
              name="nationality"
              :class="{ valid: !$v.client.nationality.$invalid, invalid: $v.client.nationality.$invalid }"
              v-model="$v.client.nationality.$model"
              id="client-nationality"
              data-cy="nationality"
            >
              <option
                v-for="nationality in nationalityValues"
                :key="nationality"
                v-bind:value="nationality"
                v-bind:label="$t('isyYachtingApp.Nationality.' + nationality)"
              >
                {{ nationality }}
              </option>
            </select>
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
            :disabled="$v.client.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./client-update.component.ts"></script>
