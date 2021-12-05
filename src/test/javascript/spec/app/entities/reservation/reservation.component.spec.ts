/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import ReservationComponent from '@/entities/reservation/reservation.vue';
import ReservationClass from '@/entities/reservation/reservation.component';
import ReservationService from '@/entities/reservation/reservation.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Reservation Management Component', () => {
    let wrapper: Wrapper<ReservationClass>;
    let comp: ReservationClass;
    let reservationServiceStub: SinonStubbedInstance<ReservationService>;

    beforeEach(() => {
      reservationServiceStub = sinon.createStubInstance<ReservationService>(ReservationService);
      reservationServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<ReservationClass>(ReservationComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          reservationService: () => reservationServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      reservationServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllReservations();
      await comp.$nextTick();

      // THEN
      expect(reservationServiceStub.retrieve.called).toBeTruthy();
      expect(comp.reservations[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      reservationServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeReservation();
      await comp.$nextTick();

      // THEN
      expect(reservationServiceStub.delete.called).toBeTruthy();
      expect(reservationServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
