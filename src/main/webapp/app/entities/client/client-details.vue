<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="client">
        <h2 class="jh-entity-heading" data-cy="clientDetailsHeading">
          <span v-text="$t('isyYachtingApp.client.detail.title')">Client</span> {{ client.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('isyYachtingApp.client.firstName')">First Name</span>
          </dt>
          <dd>
            <span>{{ client.firstName }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.client.lastName')">Last Name</span>
          </dt>
          <dd>
            <span>{{ client.lastName }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.client.phone')">Phone</span>
          </dt>
          <dd>
            <span>{{ client.phone }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.client.email')">Email</span>
          </dt>
          <dd>
            <span>{{ client.email }}</span>
          </dd>
          <dt>
            <span v-text="$t('isyYachtingApp.client.nationality')">Nationality</span>
          </dt>
          <dd>
            <span v-text="$t('isyYachtingApp.Nationality.' + client.nationality)">{{ client.nationality }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="client.id" :to="{ name: 'ClientEdit', params: { clientId: client.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./client-details.component.ts"></script>
