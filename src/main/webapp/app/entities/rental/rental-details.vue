<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="rental">
        <h2 class="jh-entity-heading" data-cy="rentalDetailsHeading">
          <span v-text="$t('isyYachtingApp.rental.detail.title')">Rental</span> {{ rental.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('isyYachtingApp.rental.beginingInstant')">Begining Instant</span>
          </dt>
          <dd>
            <span v-if="rental.beginingInstant">{{ $d(Date.parse(rental.beginingInstant), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.rental.endingInstant')">Ending Instant</span>
          </dt>
          <dd>
            <span v-if="rental.endingInstant">{{ $d(Date.parse(rental.endingInstant), 'long') }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="rental.id" :to="{ name: 'RentalEdit', params: { rentalId: rental.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./rental-details.component.ts"></script>
