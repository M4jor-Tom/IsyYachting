/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import RentalComponent from '@/entities/rental/rental.vue';
import RentalClass from '@/entities/rental/rental.component';
import RentalService from '@/entities/rental/rental.service';
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
  describe('Rental Management Component', () => {
    let wrapper: Wrapper<RentalClass>;
    let comp: RentalClass;
    let rentalServiceStub: SinonStubbedInstance<RentalService>;

    beforeEach(() => {
      rentalServiceStub = sinon.createStubInstance<RentalService>(RentalService);
      rentalServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<RentalClass>(RentalComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          rentalService: () => rentalServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      rentalServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllRentals();
      await comp.$nextTick();

      // THEN
      expect(rentalServiceStub.retrieve.called).toBeTruthy();
      expect(comp.rentals[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      rentalServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeRental();
      await comp.$nextTick();

      // THEN
      expect(rentalServiceStub.delete.called).toBeTruthy();
      expect(rentalServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
