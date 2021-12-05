/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import RentalPriceComponent from '@/entities/rental-price/rental-price.vue';
import RentalPriceClass from '@/entities/rental-price/rental-price.component';
import RentalPriceService from '@/entities/rental-price/rental-price.service';
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
  describe('RentalPrice Management Component', () => {
    let wrapper: Wrapper<RentalPriceClass>;
    let comp: RentalPriceClass;
    let rentalPriceServiceStub: SinonStubbedInstance<RentalPriceService>;

    beforeEach(() => {
      rentalPriceServiceStub = sinon.createStubInstance<RentalPriceService>(RentalPriceService);
      rentalPriceServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<RentalPriceClass>(RentalPriceComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          rentalPriceService: () => rentalPriceServiceStub,
          alertService: () => new AlertService(),
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      rentalPriceServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllRentalPrices();
      await comp.$nextTick();

      // THEN
      expect(rentalPriceServiceStub.retrieve.called).toBeTruthy();
      expect(comp.rentalPrices[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      rentalPriceServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeRentalPrice();
      await comp.$nextTick();

      // THEN
      expect(rentalPriceServiceStub.delete.called).toBeTruthy();
      expect(rentalPriceServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
